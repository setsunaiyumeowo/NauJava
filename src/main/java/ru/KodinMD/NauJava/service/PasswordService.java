package ru.KodinMD.NauJava.service;
import java.util.List;
import ru.KodinMD.NauJava.entity.GeneratedPassword;
import ru.KodinMD.NauJava.entity.PasswordAlgorithm;
public interface PasswordService {
    GeneratedPassword generate(int length,PasswordAlgorithm algorithm);
    GeneratedPassword findById(Long id);
    GeneratedPassword regenerate(Long id, int newLength);
    void deleteById(Long id);
    List<GeneratedPassword> getHistory();
}
