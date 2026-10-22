package ru.KodinMD.NauJava.repo;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.KodinMD.NauJava.entity.GeneratedPassword;
import java.util.List;
@RepositoryRestResource(path = "passwords")
public interface GeneratedPasswordRepository extends CrudRepository<GeneratedPassword, Long> {
    List<GeneratedPassword> findByLengthBetweenAndLabel(@Param("minLength") int minLength, @Param("maxLength") int maxLength, @Param("Label") String label);
    @Query("SELECT p FROM GeneratedPassword p WHERE p.user.login = :login")
    List<GeneratedPassword> findByUserLogin(@Param("login") String login);
}
