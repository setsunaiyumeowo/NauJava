package ru.KodinMD.NauJava.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.KodinMD.NauJava.config.AppInfoCFG;
import ru.KodinMD.NauJava.dao.PasswordRepository;
import ru.KodinMD.NauJava.entity.GeneratedPassword;
import ru.KodinMD.NauJava.entity.PasswordAlgorithm;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
@Service
public class PasswordServiceImpl implements PasswordService {
    private static final String DIGITS = "0123456789";
    private static final String LETTERS = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    private static final String SYMBOLS = "!@#$%^&*()_+?-=";
    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 256;
    private final PasswordRepository passwordRepository;
    private final AppInfoCFG appInfo;
    private final SecureRandom random = new SecureRandom();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Autowired
    public PasswordServiceImpl(PasswordRepository passwordRepository, AppInfoCFG appInfo){
        this.passwordRepository = passwordRepository;
        this.appInfo = appInfo;
    }
    @PostConstruct
    public void init(){
        System.out.println("App: "+appInfo.getName());
        System.out.println("Version: "+appInfo.getVersion());
    }
    @Override
    public GeneratedPassword generate(int length, PasswordAlgorithm algorithm){
        validateLength(length);
        GeneratedPassword entity = new GeneratedPassword();
        entity.setId(idCounter.getAndIncrement());
        entity.setAlgorithm(algorithm);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setLength(length);
        entity.setValue(buildPassword(length,algorithm));
        passwordRepository.create(entity);
        return entity;
    }
    private void validateLength(int length){
        if (length < MIN_LENGTH || length > MAX_LENGTH){
            throw new IllegalArgumentException("Length should be >= "+MIN_LENGTH+" but <= "+MAX_LENGTH);
        }
    }
    @Override
    public GeneratedPassword findById(Long id){
        return passwordRepository.read(id);
    }
    @Override
    public GeneratedPassword regenerate(Long id,int newLength){
        validateLength(newLength);
        GeneratedPassword existing = passwordRepository.read(id);
        if (existing == null){
            return null;
        }
        existing.setLength(newLength);
        existing.setValue(buildPassword(newLength,existing.getAlgorithm()));
        existing.setCreatedAt(LocalDateTime.now());
        passwordRepository.update(existing);
        return existing;
    }
    @Override
    public void deleteById(Long id){
        passwordRepository.delete(id);
    }
    @Override
    public List<GeneratedPassword> getHistory(){
        return passwordRepository.findAll();
    }
    private String buildPassword(int length, PasswordAlgorithm algorithm){
        String pool = switch (algorithm){
            case DIGITS -> DIGITS;
            case LETTERS -> LETTERS;
            case LETTERS_DIGITS -> DIGITS + LETTERS;
            case LETTERS_DIGITS_SYMBOLS ->  DIGITS + LETTERS + SYMBOLS;
        };
        StringBuilder passgen = new StringBuilder(length);
        for (int i = 0; i < length; i++){
            passgen.append(pool.charAt(random.nextInt(pool.length())));
        }
        return passgen.toString();
    };
}
