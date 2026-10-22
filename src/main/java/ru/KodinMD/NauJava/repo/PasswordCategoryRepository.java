package ru.KodinMD.NauJava.repo;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.CrudRepository;
import ru.KodinMD.NauJava.entity.PasswordCategory;
@RepositoryRestResource(path = "categories")
public interface PasswordCategoryRepository extends CrudRepository<PasswordCategory, Long> {
}
