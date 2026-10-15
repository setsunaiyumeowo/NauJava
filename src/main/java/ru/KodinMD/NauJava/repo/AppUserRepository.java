package ru.KodinMD.NauJava.repo;

import org.springframework.data.repository.CrudRepository;
import ru.KodinMD.NauJava.entity.AppUser;
import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findByLogin(String login);
}
