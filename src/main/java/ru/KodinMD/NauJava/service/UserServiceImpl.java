package ru.KodinMD.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.KodinMD.NauJava.entity.AppUser;
import ru.KodinMD.NauJava.entity.Role;
import ru.KodinMD.NauJava.repo.AppUserRepository;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private static final int MIN_PASSWORD_LENGTH = 6;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(AppUserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public Optional<AppUser> findByLogin(String login){
        return userRepository.findByLogin(login);
    }
    @Override
    @Transactional
    public AppUser addUser(String login, String email, String rawPassword){
        if (login == null || login.isBlank()){
            throw new IllegalArgumentException("Login must not be empty");
        }
        if (email == null || email.isBlank()){
            throw new IllegalArgumentException("Email must not be empty");
        }
        if (rawPassword == null || rawPassword.length() < MIN_PASSWORD_LENGTH){
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
        String cleanLogin = login.trim();
        if (userRepository.findByLogin(cleanLogin).isPresent()){
            throw new IllegalArgumentException("User already exists: " + cleanLogin);
        }
        AppUser user = new AppUser();
        user.setLogin(cleanLogin);
        user.setEmail(email.trim());
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
}
