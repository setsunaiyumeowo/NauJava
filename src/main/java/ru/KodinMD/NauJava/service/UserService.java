package ru.KodinMD.NauJava.service;

import ru.KodinMD.NauJava.entity.AppUser;
import java.util.Optional;
public interface UserService {
    Optional<AppUser> findByLogin(String login);
    AppUser addUser(String login, String email, String rawPassword);
}
