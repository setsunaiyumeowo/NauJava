package ru.KodinMD.NauJava.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.KodinMD.NauJava.service.UserService;
@Controller
public class AuthController {
    private final UserService userService;
    @Autowired
    public AuthController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/registration")
    public String registrationForm(){
        return "registration";
    }
    @PostMapping("/registration")
    public String register(@RequestParam String login,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model){
        try {
            userService.addUser(login, email, password);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e){
            model.addAttribute("message", e.getMessage());
            model.addAttribute("login", login);
            model.addAttribute("email", email);
            return "registration";
        }
    }
}
