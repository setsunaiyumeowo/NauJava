package ru.KodinMD.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import ru.KodinMD.NauJava.entity.GeneratedPassword;
import ru.KodinMD.NauJava.repo.GeneratedPasswordRepository;

@Controller
@RequestMapping("/view/passwords")
public class PasswordViewController {
    private final GeneratedPasswordRepository passwordRepository;
    @Autowired
    public PasswordViewController(GeneratedPasswordRepository passwordRepository){
        this.passwordRepository = passwordRepository;
    }
    @GetMapping("/list")
    public String passwordList(Model model){
        Iterable<GeneratedPassword> passwords = passwordRepository.findAll();
        model.addAttribute("passwords", passwords);
        return "passwordsList";
    }
}
