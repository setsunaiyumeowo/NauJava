package ru.KodinMD.NauJava;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.KodinMD.NauJava.service.UserService;
import java.time.Duration;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthUiTest {
    private static final String PASSWORD = "secret123";
    private final UserService userService;
    @Value("${local.server.port}")
    private int port;
    private WebDriver driver;
    private WebDriverWait wait;
    private String login;
    @Autowired
    AuthUiTest(UserService userService){
        this.userService = userService;
    }
    @BeforeEach
    void setUp(){
        login = UUID.randomUUID().toString();
        userService.addUser(login, login + "@test.ru", PASSWORD);
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    @AfterEach
    void tearDown(){
        if (driver != null){
            driver.quit();
        }
    }
    private String url(String path){
        return "http://localhost:" + port + path;
    }
    private void signIn(String password){
        driver.get(url("/login"));
        driver.findElement(By.name("username")).sendKeys(login);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }
    @Test
    void testLogin(){
        signIn(PASSWORD);
        wait.until(ExpectedConditions.urlContains("/view/passwords/list"));
        Assertions.assertEquals("Generated passwords", driver.findElement(By.tagName("h1")).getText());
        Assertions.assertTrue(driver.findElement(By.cssSelector("form[action$='/logout'] button")).isDisplayed());
    }
    @Test
    void testLoginWithWrongPassword(){
        signIn("wrong" + PASSWORD);
        wait.until(ExpectedConditions.urlContains("/login?error"));
        Assertions.assertTrue(driver.getPageSource().contains("Неверный логин или пароль"));
    }
    @Test
    void testLogout(){
        signIn(PASSWORD);
        wait.until(ExpectedConditions.urlContains("/view/passwords/list"));
        driver.findElement(By.cssSelector("form[action$='/logout'] button")).click();
        wait.until(ExpectedConditions.urlContains("/login?logout"));
        Assertions.assertTrue(driver.getPageSource().contains("Вы вышли из системы"));
        driver.get(url("/view/passwords/list"));
        wait.until(ExpectedConditions.urlMatches(".*/login$"));
        Assertions.assertTrue(driver.findElement(By.name("username")).isDisplayed());
    }
}
