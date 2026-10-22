package ru.KodinMD.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.KodinMD.NauJava.dao.GeneratedPasswordRepositoryCustom;
import ru.KodinMD.NauJava.dto.PasswordDto;
import ru.KodinMD.NauJava.entity.GeneratedPassword;
import ru.KodinMD.NauJava.exception.ResourceNotFoundException;
import ru.KodinMD.NauJava.repo.AppUserRepository;
import ru.KodinMD.NauJava.repo.GeneratedPasswordRepository;
import java.util.List;

@RestController
@RequestMapping("/custom/passwords")
public class PasswordRestController {
    private final GeneratedPasswordRepository passwordRepository;
    private final GeneratedPasswordRepositoryCustom passwordRepositoryCustom;
    private final AppUserRepository userRepository;
    @Autowired
    public PasswordRestController(GeneratedPasswordRepository passwordRepository,
                                  GeneratedPasswordRepositoryCustom passwordRepositoryCustom,
                                  AppUserRepository userRepository){
        this.passwordRepository = passwordRepository;
        this.passwordRepositoryCustom = passwordRepositoryCustom;
        this.userRepository = userRepository;
    }
    @GetMapping("/findByLengthAndLabel")
    public List<PasswordDto> findByLengthAndLabel(@RequestParam int minLength,
                                                  @RequestParam int maxLength,
                                                  @RequestParam String label) {
        checkRange(minLength, maxLength);
        return toDto(passwordRepository.findByLengthBetweenAndLabel(minLength, maxLength, label));
    }
    @GetMapping("/findByUserLogin")
    public List<PasswordDto> findByUserLogin(@RequestParam String login) {
        checkUserExists(login);
        return toDto(passwordRepository.findByUserLogin(login));
    }
    @GetMapping("/criteria/findByLengthAndLabel")
    public List<PasswordDto> criteriaFindByLengthAndLabel(@RequestParam int minLength,
                                                          @RequestParam int maxLength,
                                                          @RequestParam String label) {
        checkRange(minLength, maxLength);
        return toDto(passwordRepositoryCustom.findByLengthBetweenAndLabel(minLength, maxLength, label));
    }
    @GetMapping("/criteria/findByUserLogin")
    public List<PasswordDto> criteriaFindByUserLogin(@RequestParam String login) {
        checkUserExists(login);
        return toDto(passwordRepositoryCustom.findByUserLogin(login));
    }
    private void checkRange(int minLength, int maxLength) {
        if (minLength > maxLength) {
            throw new IllegalArgumentException("minLength must be <= maxLength");
        }
    }
    private void checkUserExists(String login) {
        userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + login));
    }
    private List<PasswordDto> toDto(List<GeneratedPassword> passwords) {
        return passwords.stream().map(PasswordDto::from).toList();
    }
}
