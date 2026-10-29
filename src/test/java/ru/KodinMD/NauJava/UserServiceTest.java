package ru.KodinMD.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.KodinMD.NauJava.entity.AppUser;
import ru.KodinMD.NauJava.entity.Role;
import ru.KodinMD.NauJava.service.UserService;
import java.util.UUID;

@SpringBootTest
class UserServiceTest {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    UserServiceTest(UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @Test
    void testAddAndFindUser(){
        String login = UUID.randomUUID().toString();
        userService.addUser(login, login + "@test.ru", "secret123");
        AppUser found = userService.findByLogin(login).orElseThrow();
        Assertions.assertEquals(login, found.getLogin());
        Assertions.assertEquals(Role.USER, found.getRole());
        Assertions.assertNotEquals("secret123", found.getPassword());
        Assertions.assertTrue(passwordEncoder.matches("secret123", found.getPassword()));
    }
    @Test
    void testAddDuplicateUser(){
        String login = UUID.randomUUID().toString();
        userService.addUser(login, login + "@test.ru", "secret123");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.addUser(login, login + "@test.ru", "other123"));
    }
    @Test
    void testShortPassword(){
        String login = UUID.randomUUID().toString();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.addUser(login, login + "@test.ru", "123"));
        Assertions.assertTrue(userService.findByLogin(login).isEmpty());
    }
    @Test
    void testFindUnknownUser(){
        Assertions.assertTrue(userService.findByLogin(UUID.randomUUID().toString()).isEmpty());
    }
}
