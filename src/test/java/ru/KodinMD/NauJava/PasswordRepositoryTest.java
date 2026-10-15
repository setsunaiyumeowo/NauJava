package ru.KodinMD.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.KodinMD.NauJava.dao.GeneratedPasswordRepositoryCustom;
import ru.KodinMD.NauJava.entity.AppUser;
import ru.KodinMD.NauJava.entity.GeneratedPassword;
import ru.KodinMD.NauJava.entity.GenerationAlgorithm;
import ru.KodinMD.NauJava.repo.AppUserRepository;
import ru.KodinMD.NauJava.repo.GeneratedPasswordRepository;
import ru.KodinMD.NauJava.repo.GenerationAlgorithmRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class PasswordRepositoryTest {
    private final AppUserRepository userRepository;
    private final GenerationAlgorithmRepository algorithmRepository;
    private final GeneratedPasswordRepository passwordRepository;
    private final GeneratedPasswordRepositoryCustom passwordRepositoryCustom;
    @Autowired
    PasswordRepositoryTest(AppUserRepository userRepository, GenerationAlgorithmRepository algorithmRepository, GeneratedPasswordRepository passwordRepository, GeneratedPasswordRepositoryCustom passwordRepositoryCustom) {
        this.userRepository = userRepository;
        this.algorithmRepository = algorithmRepository;
        this.passwordRepository = passwordRepository;
        this.passwordRepositoryCustom = passwordRepositoryCustom;
    }
    private AppUser createUser() {
        AppUser user = new AppUser();
        user.setLogin(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID() + "@test.ru");
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    private GenerationAlgorithm createAlgorithm() {
        GenerationAlgorithm algorithm = new GenerationAlgorithm();
        algorithm.setName(UUID.randomUUID().toString());
        algorithm.setDescription("Test");
        algorithm.setAlphabet("abcdef0123456789");
        return algorithmRepository.save(algorithm);
    }
    private GeneratedPassword createPassword(AppUser user, GenerationAlgorithm algorithm, String label, int length) {
        GeneratedPassword password = new GeneratedPassword();
        password.setUser(user);
        password.setAlgorithm(algorithm);
        password.setLabel(label);
        password.setLength(length);
        password.setSecretValue("x".repeat(length));
        password.setCreatedAt(LocalDateTime.now());
        return passwordRepository.save(password);
    }
    private GeneratedPassword prepareLengthData(String label) {
        AppUser user = createUser();
        GenerationAlgorithm algorithm = createAlgorithm();
        createPassword(user, algorithm, label, 8);
        GeneratedPassword expected = createPassword(user, algorithm, label, 12);
        createPassword(user, algorithm, label, 20);
        createPassword(user, algorithm, UUID.randomUUID().toString(), 12); // другая метка
        return expected;
    }
    private AppUser prepareUserData() {
        AppUser firstUser = createUser();
        AppUser secondUser = createUser();
        GenerationAlgorithm algorithm = createAlgorithm();
        createPassword(firstUser, algorithm, "Gmail", 10);
        createPassword(firstUser, algorithm, "GitHub", 14);
        createPassword(secondUser, algorithm, "Gmail", 10);
        return firstUser;
    }
    @Test
    void testFindByLengthBetweenAndLabel() {
        String label = UUID.randomUUID().toString();
        GeneratedPassword expected = prepareLengthData(label);
        List<GeneratedPassword> found = passwordRepository.findByLengthBetweenAndLabel(10, 15, label);
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals(expected.getId(), found.get(0).getId());
        Assertions.assertEquals(12, found.get(0).getLength());
    }
    @Test
    void testFindByUserLogin() {
        AppUser user = prepareUserData();
        List<GeneratedPassword> found = passwordRepository.findByUserLogin(user.getLogin());
        Assertions.assertEquals(2, found.size());
        for (GeneratedPassword password : found) {
            Assertions.assertEquals(user.getId(), password.getUser().getId());
        }
    }
    @Test
    void testCriteriaFindByLengthBetweenAndLabel() {
        String label = UUID.randomUUID().toString();
        GeneratedPassword expected = prepareLengthData(label);
        List<GeneratedPassword> found = passwordRepositoryCustom.findByLengthBetweenAndLabel(10, 15, label);
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals(expected.getId(), found.get(0).getId());
        Assertions.assertEquals(12, found.get(0).getLength());
    }
    @Test
    void testCriteriaFindByUserLogin() {
        AppUser user = prepareUserData();
        List<GeneratedPassword> found = passwordRepositoryCustom.findByUserLogin(user.getLogin());
        Assertions.assertEquals(2, found.size());
        for (GeneratedPassword password : found) {
            Assertions.assertEquals(user.getId(), password.getUser().getId());
        }
    }
}
