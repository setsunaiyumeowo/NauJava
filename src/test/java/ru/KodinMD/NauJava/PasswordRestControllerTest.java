package ru.KodinMD.NauJava;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.KodinMD.NauJava.entity.GenerationAlgorithm;
import ru.KodinMD.NauJava.repo.GenerationAlgorithmRepository;
import ru.KodinMD.NauJava.service.PasswordService;
import ru.KodinMD.NauJava.service.UserService;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PasswordRestControllerTest {
    private static final String PASSWORD = "secret123";
    private final UserService userService;
    private final PasswordService passwordService;
    private final GenerationAlgorithmRepository algorithmRepository;
    @Value("${local.server.port}")
    private int port;
    private String login;
    private String label;
    @Autowired
    PasswordRestControllerTest(UserService userService, PasswordService passwordService, GenerationAlgorithmRepository algorithmRepository){
        this.userService = userService;
        this.passwordService = passwordService;
        this.algorithmRepository = algorithmRepository;
    }
    @BeforeEach
    void setUp(){
        RestAssured.port = port;
        login = UUID.randomUUID().toString();
        label = UUID.randomUUID().toString();
        userService.addUser(login, login + "@test.ru", PASSWORD);
        GenerationAlgorithm algorithm = new GenerationAlgorithm();
        algorithm.setName(UUID.randomUUID().toString());
        algorithm.setDescription("Test");
        algorithm.setAlphabet("abcdef0123456789");
        algorithmRepository.save(algorithm);
        passwordService.generate(login, algorithm.getName(), 12, label);
    }
    private RequestSpecification authorized(){
        Response page = RestAssured.given().get("/login");
        String csrf = page.htmlPath().getString("**.find { it.@name == '_csrf' }.@value");
        Response response = RestAssured.given()
                .redirects().follow(false)
                .cookies(page.getCookies())
                .formParam("username", login)
                .formParam("password", PASSWORD)
                .formParam("_csrf", csrf)
                .post("/login");
        return RestAssured.given().cookies(response.getCookies());
    }
    @Test
    void testFindByUserLogin(){
        authorized()
                .queryParam("login", login)
                .when().get("/custom/passwords/findByUserLogin")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", Matchers.equalTo(1))
                .body("[0].label", Matchers.equalTo(label))
                .body("[0].length", Matchers.equalTo(12))
                .body("[0].userLogin", Matchers.equalTo(login));
    }
    @Test
    void testFindByUnknownUserLogin(){
        String unknown = UUID.randomUUID().toString();
        authorized()
                .queryParam("login", unknown)
                .when().get("/custom/passwords/findByUserLogin")
                .then()
                .statusCode(404)
                .body("message", Matchers.equalTo("User not found: " + unknown));
    }
    @Test
    void testFindByUserLoginWithoutParameter(){
        authorized()
                .when().get("/custom/passwords/findByUserLogin")
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("Missing parameter: login"));
    }
    @Test
    void testFindByLengthAndLabel(){
        authorized()
                .queryParam("minLength", 10)
                .queryParam("maxLength", 15)
                .queryParam("label", label)
                .when().get("/custom/passwords/findByLengthAndLabel")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(1))
                .body("[0].label", Matchers.equalTo(label));
    }
    @Test
    void testFindByLengthOnBorder(){
        authorized()
                .queryParam("minLength", 12)
                .queryParam("maxLength", 12)
                .queryParam("label", label)
                .when().get("/custom/passwords/findByLengthAndLabel")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(1));
    }
    @Test
    void testFindByLengthOutOfRange(){
        authorized()
                .queryParam("minLength", 13)
                .queryParam("maxLength", 20)
                .queryParam("label", label)
                .when().get("/custom/passwords/findByLengthAndLabel")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(0));
    }
    @Test
    void testFindByLengthWrongRange(){
        authorized()
                .queryParam("minLength", 15)
                .queryParam("maxLength", 10)
                .queryParam("label", label)
                .when().get("/custom/passwords/findByLengthAndLabel")
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("minLength must be <= maxLength"));
    }
    @Test
    void testFindByLengthWrongType(){
        authorized()
                .queryParam("minLength", "abc")
                .queryParam("maxLength", 10)
                .queryParam("label", label)
                .when().get("/custom/passwords/findByLengthAndLabel")
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("Wrong value for parameter: minLength"));
    }
    @Test
    void testCriteriaFindByUserLogin(){
        authorized()
                .queryParam("login", login)
                .when().get("/custom/passwords/criteria/findByUserLogin")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(1))
                .body("[0].userLogin", Matchers.equalTo(login));
    }
    @Test
    void testWithoutAuthorization(){
        RestAssured.given()
                .redirects().follow(false)
                .queryParam("login", login)
                .when().get("/custom/passwords/findByUserLogin")
                .then()
                .statusCode(302)
                .header("Location", Matchers.endsWith("/login"));
    }
}
