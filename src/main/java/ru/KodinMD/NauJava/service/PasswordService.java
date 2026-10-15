package ru.KodinMD.NauJava.service;
import java.util.List;
import ru.KodinMD.NauJava.entity.GeneratedPassword;
import ru.KodinMD.NauJava.entity.GenerationHistory;

public interface PasswordService {
    GeneratedPassword generate(String userLogin, String algorithmName, int length, String label);
    GeneratedPassword createUserWithFirstPassword(String login, String email, String algorithmName, int length, String label);
    GeneratedPassword regenerate(Long passwordId, int newLength);
    List<GenerationHistory> getHistory(Long passwordId);
}
