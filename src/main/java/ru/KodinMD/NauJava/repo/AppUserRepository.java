package ru.KodinMD.NauJava.repo;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import org.springframework.data.repository.CrudRepository;
import ru.KodinMD.NauJava.entity.AppUser;
import java.util.Optional;
@RepositoryRestResource(path = "users")
public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findByLogin(String login);
}
