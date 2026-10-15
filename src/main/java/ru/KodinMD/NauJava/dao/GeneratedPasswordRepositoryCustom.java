package ru.KodinMD.NauJava.dao;

import ru.KodinMD.NauJava.entity.GeneratedPassword;
import java.util.List;

public interface GeneratedPasswordRepositoryCustom {
    List<GeneratedPassword> findByLengthBetweenAndLabel(int minLength, int maxLength, String label);
    List<GeneratedPassword> findByUserLogin(String login);
}
