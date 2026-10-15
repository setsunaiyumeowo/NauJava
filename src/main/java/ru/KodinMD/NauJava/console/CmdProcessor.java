/*
package ru.KodinMD.NauJava.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.KodinMD.NauJava.entity.PasswordAlgorithm;
import ru.KodinMD.NauJava.entity.GeneratedPassword;
import ru.KodinMD.NauJava.service.PasswordService;

import java.util.Arrays;
import java.util.List;

@Component
public class CmdProcessor {
    private final PasswordService passwordService;
    private PasswordAlgorithm parseAlgorithm(String error_text) {
        try {
            return PasswordAlgorithm.valueOf(error_text.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unknown algorithm '" + error_text + "'. Available: " + Arrays.toString(PasswordAlgorithm.values()));
        }
    }
    @Autowired
    public CmdProcessor(PasswordService passwordService){
        this.passwordService = passwordService;
    }
    public void processCommand(String input){
        String[] cmd = input.trim().split("\\s+");
        try {
            switch (cmd[0]){
                case "!generate" -> {
                    GeneratedPassword password = passwordService.generate(Integer.parseInt(cmd[1]),parseAlgorithm(cmd[2]));
                    System.out.println("Generated password: " + format(password));
                }
                case "!get" -> {
                    GeneratedPassword password = passwordService.findById(Long.valueOf(cmd[1]));
                    System.out.println( password == null ? "Password not found" : format(password));
                }
                case "!regenerate" -> {
                    GeneratedPassword password = passwordService.regenerate(Long.valueOf(cmd[1]),Integer.parseInt(cmd[2]));
                    System.out.println( password == null ? "Password not found" : "Password regenerated: " + format(password));
                }
                case "delete" -> {
                    passwordService.deleteById(Long.valueOf(cmd[1]));
                    System.out.println("Generated password deleted");
                }
                case "!history" -> {
                    List<GeneratedPassword> list = passwordService.getHistory();
                    if (list.isEmpty()) {
                        System.out.println("No password was generated. So what are you waiting for?");
                    }
                    list.forEach(password -> System.out.println(format(password)));
                }
                case "!help" -> {
                    System.out.println("Command list");
                    System.out.println("!generate <length> <DIGITS|LETTERS|LETTERS_DIGITS|LETTERS_DIGITS_SYMBOLS");
                    System.out.println("!get <id>");
                    System.out.println("!regenerate <id> <length>");
                    System.out.println("!history");
                    System.out.println("!exit");
                }
                default -> System.out.println("Command not found. Try !help");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Not enough arguments. Try !help");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private String format(GeneratedPassword password) {
        return "№" + password.getId() + " " + password.getValue() + " [" + password.getAlgorithm() + ", " + password.getLength() + ", " + password.getCreatedAt() + "]";
    }
}
*/
