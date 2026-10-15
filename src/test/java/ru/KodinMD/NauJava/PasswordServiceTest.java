package ru.KodinMD.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.KodinMD.NauJava.entity.GeneratedPassword;
import ru.KodinMD.NauJava.entity.GenerationAlgorithm;
import ru.KodinMD.NauJava.entity.GenerationHistory;
import ru.KodinMD.NauJava.entity.HistoryAction;
import ru.KodinMD.NauJava.repo.AppUserRepository;
import ru.KodinMD.NauJava.repo.GeneratedPasswordRepository;
import ru.KodinMD.NauJava.repo.GenerationAlgorithmRepository;
import ru.KodinMD.NauJava.repo.GenerationHistoryRepository;
import ru.KodinMD.NauJava.service.PasswordService;

import java.util.List;
import java.util.UUID;

@SpringBootTest
public class PasswordServiceTest {
    private static final String ALPHABET = "abcdef0123456789";
    private final PasswordService passwordService;
    private final AppUserRepository userRepository;
    private final GenerationAlgorithmRepository algorithmRepository;
    private final GeneratedPasswordRepository passwordRepository;
    private final GenerationHistoryRepository historyRepository;
    @Autowired
    PasswordServiceTest(PasswordService passwordService,
                        AppUserRepository userRepository,
                        GenerationAlgorithmRepository algorithmRepository,
                        GeneratedPasswordRepository passwordRepository,
                        GenerationHistoryRepository historyRepository) {
        this.passwordService = passwordService;
        this.userRepository = userRepository;
        this.algorithmRepository = algorithmRepository;
        this.passwordRepository = passwordRepository;
        this.historyRepository = historyRepository;
    }
    private GenerationAlgorithm createAlgorithm() {
        GenerationAlgorithm algorithm = new GenerationAlgorithm();
        algorithm.setName(UUID.randomUUID().toString());
        algorithm.setDescription("Тестовый алгоритм");
        algorithm.setAlphabet(ALPHABET);
        return algorithmRepository.save(algorithm);
    }
    @Test
    void testCreateUserWithFirstPassword() {
        GenerationAlgorithm algorithm = createAlgorithm();
        String login = UUID.randomUUID().toString();
        GeneratedPassword password = passwordService.createUserWithFirstPassword(
                login, login + "@test.ru", algorithm.getName(), 12, "Gmail");
        Assertions.assertTrue(userRepository.findByLogin(login).isPresent());
        Assertions.assertTrue(passwordRepository.findById(password.getId()).isPresent());
        Assertions.assertEquals(12, password.getSecretValue().length());
        Assertions.assertTrue(password.getSecretValue().chars()
                .allMatch(symbol -> ALPHABET.indexOf(symbol) >= 0));
        List<GenerationHistory> history = historyRepository.findByPasswordIdOrderByIdAsc(password.getId());
        Assertions.assertEquals(1, history.size());
        Assertions.assertEquals(HistoryAction.GENERATED, history.get(0).getAction());
    }
    @Test
    void testCreateUserWithFirstPasswordRollback() {
        GenerationAlgorithm algorithm = createAlgorithm();
        String login = UUID.randomUUID().toString();
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                passwordService.createUserWithFirstPassword(
                        login, login + "@test.ru", algorithm.getName(), 1, "Gmail"));
        Assertions.assertTrue(userRepository.findByLogin(login).isEmpty());
    }
    @Test
    void testRegenerate() {
        GenerationAlgorithm algorithm = createAlgorithm();
        String login = UUID.randomUUID().toString();
        GeneratedPassword password = passwordService.createUserWithFirstPassword(
                login, login + "@test.ru", algorithm.getName(), 12, "Gmail");
        GeneratedPassword updated = passwordService.regenerate(password.getId(), 20);
        Assertions.assertEquals(20, updated.getLength());
        Assertions.assertEquals(20, updated.getSecretValue().length());
        List<GenerationHistory> history = passwordService.getHistory(password.getId());
        Assertions.assertEquals(2, history.size());
        Assertions.assertEquals(HistoryAction.GENERATED, history.get(0).getAction());
        Assertions.assertEquals(HistoryAction.REGENERATED, history.get(1).getAction());
    }
}
