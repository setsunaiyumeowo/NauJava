package ru.KodinMD.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.KodinMD.NauJava.config.AppInfoCFG;
import ru.KodinMD.NauJava.entity.AppUser;
import ru.KodinMD.NauJava.entity.GeneratedPassword;
import ru.KodinMD.NauJava.entity.GenerationAlgorithm;
import ru.KodinMD.NauJava.entity.GenerationHistory;
import ru.KodinMD.NauJava.entity.HistoryAction;
import ru.KodinMD.NauJava.repo.AppUserRepository;
import ru.KodinMD.NauJava.repo.GeneratedPasswordRepository;
import ru.KodinMD.NauJava.repo.GenerationAlgorithmRepository;
import ru.KodinMD.NauJava.repo.GenerationHistoryRepository;
import ru.KodinMD.NauJava.service.PasswordServiceImpl;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PasswordServiceUnitTest {
    private static final String ALPHABET = "abcdef0123456789";
    @Mock
    private AppUserRepository userRepository;
    @Mock
    private GenerationAlgorithmRepository algorithmRepository;
    @Mock
    private GeneratedPasswordRepository passwordRepository;
    @Mock
    private GenerationHistoryRepository historyRepository;
    @Mock
    private AppInfoCFG appInfo;
    @InjectMocks
    private PasswordServiceImpl passwordService;
    private AppUser createUser(){
        AppUser user = new AppUser();
        user.setLogin("tester");
        return user;
    }
    private GenerationAlgorithm createAlgorithm(){
        GenerationAlgorithm algorithm = new GenerationAlgorithm();
        algorithm.setName("hex");
        algorithm.setAlphabet(ALPHABET);
        return algorithm;
    }
    private GeneratedPassword createPassword(){
        GeneratedPassword password = new GeneratedPassword();
        password.setAlgorithm(createAlgorithm());
        password.setLength(12);
        password.setSecretValue("abcdef012345");
        return password;
    }
    @Test
    void testGenerate(){
        AppUser user = createUser();
        Mockito.when(userRepository.findByLogin("tester")).thenReturn(Optional.of(user));
        Mockito.when(algorithmRepository.findByName("hex")).thenReturn(Optional.of(createAlgorithm()));
        GeneratedPassword password = passwordService.generate("tester", "hex", 12, "Gmail");
        Assertions.assertEquals(user, password.getUser());
        Assertions.assertEquals("Gmail", password.getLabel());
        Assertions.assertEquals(12, password.getLength());
        Assertions.assertEquals(12, password.getSecretValue().length());
        Assertions.assertTrue(password.getSecretValue().chars()
                .allMatch(symbol -> ALPHABET.indexOf(symbol) >= 0));
        Mockito.verify(passwordRepository).save(password);
        ArgumentCaptor<GenerationHistory> captor = ArgumentCaptor.forClass(GenerationHistory.class);
        Mockito.verify(historyRepository).save(captor.capture());
        Assertions.assertEquals(HistoryAction.GENERATED, captor.getValue().getAction());
        Assertions.assertEquals(12, captor.getValue().getLength());
    }
    @Test
    void testGenerateUnknownUser(){
        Mockito.when(userRepository.findByLogin("nobody")).thenReturn(Optional.empty());
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class,
                () -> passwordService.generate("nobody", "hex", 12, "Gmail"));
        Assertions.assertEquals("User not found: nobody", e.getMessage());
        Mockito.verify(passwordRepository, Mockito.never()).save(Mockito.any());
    }
    @Test
    void testGenerateUnknownAlgorithm(){
        Mockito.when(userRepository.findByLogin("tester")).thenReturn(Optional.of(createUser()));
        Mockito.when(algorithmRepository.findByName("unknown")).thenReturn(Optional.empty());
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class,
                () -> passwordService.generate("tester", "unknown", 12, "Gmail"));
        Assertions.assertEquals("Unknown algorithm: unknown", e.getMessage());
        Mockito.verify(passwordRepository, Mockito.never()).save(Mockito.any());
    }
    @Test
    void testGenerateTooShort(){
        Mockito.when(userRepository.findByLogin("tester")).thenReturn(Optional.of(createUser()));
        Mockito.when(algorithmRepository.findByName("hex")).thenReturn(Optional.of(createAlgorithm()));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> passwordService.generate("tester", "hex", 5, "Gmail"));
        Mockito.verify(passwordRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(historyRepository, Mockito.never()).save(Mockito.any());
    }
    @Test
    void testGenerateTooLong(){
        Mockito.when(userRepository.findByLogin("tester")).thenReturn(Optional.of(createUser()));
        Mockito.when(algorithmRepository.findByName("hex")).thenReturn(Optional.of(createAlgorithm()));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> passwordService.generate("tester", "hex", 257, "Gmail"));
        Mockito.verify(passwordRepository, Mockito.never()).save(Mockito.any());
    }
    @Test
    void testGenerateMinLength(){
        Mockito.when(userRepository.findByLogin("tester")).thenReturn(Optional.of(createUser()));
        Mockito.when(algorithmRepository.findByName("hex")).thenReturn(Optional.of(createAlgorithm()));
        GeneratedPassword password = passwordService.generate("tester", "hex", 6, "Gmail");
        Assertions.assertEquals(6, password.getSecretValue().length());
    }
    @Test
    void testRegenerate(){
        GeneratedPassword password = createPassword();
        Mockito.when(passwordRepository.findById(1L)).thenReturn(Optional.of(password));
        GeneratedPassword updated = passwordService.regenerate(1L, 20);
        Assertions.assertEquals(20, updated.getLength());
        Assertions.assertEquals(20, updated.getSecretValue().length());
        Mockito.verify(passwordRepository).save(password);
        ArgumentCaptor<GenerationHistory> captor = ArgumentCaptor.forClass(GenerationHistory.class);
        Mockito.verify(historyRepository).save(captor.capture());
        Assertions.assertEquals(HistoryAction.REGENERATED, captor.getValue().getAction());
    }
    @Test
    void testRegenerateUnknownPassword(){
        Mockito.when(passwordRepository.findById(99L)).thenReturn(Optional.empty());
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class,
                () -> passwordService.regenerate(99L, 20));
        Assertions.assertEquals("Password not found: 99", e.getMessage());
        Mockito.verify(historyRepository, Mockito.never()).save(Mockito.any());
    }
    @Test
    void testRegenerateWrongLength(){
        GeneratedPassword password = createPassword();
        Mockito.when(passwordRepository.findById(1L)).thenReturn(Optional.of(password));
        Assertions.assertThrows(IllegalArgumentException.class, () -> passwordService.regenerate(1L, 1));
        Assertions.assertEquals(12, password.getLength());
        Assertions.assertEquals("abcdef012345", password.getSecretValue());
        Mockito.verify(passwordRepository, Mockito.never()).save(Mockito.any());
    }
    @Test
    void testGetHistory(){
        List<GenerationHistory> history = List.of(new GenerationHistory(), new GenerationHistory());
        Mockito.when(historyRepository.findByPasswordIdOrderByIdAsc(1L)).thenReturn(history);
        Assertions.assertEquals(history, passwordService.getHistory(1L));
    }
}
