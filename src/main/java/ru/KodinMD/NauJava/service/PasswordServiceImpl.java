package ru.KodinMD.NauJava.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.KodinMD.NauJava.config.AppInfoCFG;
import ru.KodinMD.NauJava.entity.*;
import ru.KodinMD.NauJava.repo.GeneratedPasswordRepository;
import ru.KodinMD.NauJava.repo.AppUserRepository;
import ru.KodinMD.NauJava.repo.GenerationAlgorithmRepository;
import ru.KodinMD.NauJava.repo.GenerationHistoryRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class PasswordServiceImpl implements PasswordService {
    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 256;
    private final GenerationAlgorithmRepository algorithmRepository;
    private final GeneratedPasswordRepository passwordRepository;
    private final GenerationHistoryRepository historyRepository;
    private final AppUserRepository userRepository;
    private final AppInfoCFG appInfo;
    private final SecureRandom random = new SecureRandom();

    @Autowired
    public PasswordServiceImpl(AppUserRepository userRepository, GenerationAlgorithmRepository algorithmRepository, GeneratedPasswordRepository passwordRepository, GenerationHistoryRepository historyRepository, AppInfoCFG appInfo){
        this.userRepository = userRepository;
        this.algorithmRepository = algorithmRepository;
        this.historyRepository = historyRepository;
        this.passwordRepository = passwordRepository;
        this.appInfo = appInfo;
    }
    @PostConstruct
    public void init(){
        System.out.println("App: "+appInfo.getName());
        System.out.println("Version: "+appInfo.getVersion());
    }
    @Override
    @Transactional
    public GeneratedPassword generate(String userLogin, String algorithmName, int length, String label) {
        AppUser user = userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userLogin));
        return createPassword(user, algorithmName, length, label);
    }
    @Override
    @Transactional
    public GeneratedPassword createUserWithFirstPassword(String login, String email, String algorithmName, int length, String label){
        AppUser user = new AppUser();
        user.setLogin(login);
        user.setEmail(email);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return createPassword(user,algorithmName,length,label);
    }
    private void validateLength(int length){
        if (length < MIN_LENGTH || length > MAX_LENGTH){
            throw new IllegalArgumentException("Length must be >= "+MIN_LENGTH+" but <= "+MAX_LENGTH);
        }
    }
    @Override
    @Transactional
    public GeneratedPassword regenerate(Long passwordId,int newLength){
        GeneratedPassword password = passwordRepository.findById(passwordId).orElseThrow(() -> new IllegalArgumentException("Password not found: " + passwordId));
        validateLength(newLength);
        password.setLength(newLength);
        password.setSecretValue(buildPassword(newLength, password.getAlgorithm().getAlphabet()));
        passwordRepository.save(password);
        saveHistory(password, HistoryAction.REGENERATED);
        return password;
    }
    @Override
    public List<GenerationHistory> getHistory(Long passwordId) {
        return historyRepository.findByPasswordIdOrderByIdAsc(passwordId);
    }

    private GeneratedPassword createPassword(AppUser user, String algorithmName, int length, String label) {
        GenerationAlgorithm algorithm = algorithmRepository.findByName(algorithmName)
                .orElseThrow(() -> new IllegalArgumentException("Unknown algorithm: " + algorithmName));
        validateLength(length);

        GeneratedPassword password = new GeneratedPassword();
        password.setUser(user);
        password.setAlgorithm(algorithm);
        password.setLabel(label);
        password.setLength(length);
        password.setSecretValue(buildPassword(length, algorithm.getAlphabet()));
        password.setCreatedAt(LocalDateTime.now());
        passwordRepository.save(password);

        saveHistory(password, HistoryAction.GENERATED);
        return password;
    }
    private void saveHistory(GeneratedPassword password, HistoryAction action){
        GenerationHistory record = new GenerationHistory();
        record.setPassword(password);
        record.setAction(action);
        record.setLength(password.getLength());
        record.setCreatedAt(LocalDateTime.now());
        historyRepository.save(record);
    }
    private String buildPassword(int length, String alphabet) {
        StringBuilder passgen = new StringBuilder(length);
        for (int i = 0; i < length; i++){
            passgen.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return passgen.toString();
    }
}
