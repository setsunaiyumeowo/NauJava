package ru.KodinMD.NauJava.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.KodinMD.NauJava.entity.AppUser;
import ru.KodinMD.NauJava.entity.Role;
import ru.KodinMD.NauJava.repo.AppUserRepository;
import java.time.LocalDateTime;
@Component
public class AdminInitializer implements CommandLineRunner{
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.admin.login}")
    private String adminLogin;
    @Value("${app.admin.email}")
    private String adminEmail;
    @Value("${app.admin.password}")
    private String adminPassword;
    @Autowired
    public AdminInitializer(AppUserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void run(String... args){
        if (userRepository.findByLogin(adminLogin).isPresent()){
            return;
        }
        AppUser admin = new AppUser();
        admin.setLogin(adminLogin);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);
        admin.setCreatedAt(LocalDateTime.now());
        userRepository.save(admin);
    }
}
