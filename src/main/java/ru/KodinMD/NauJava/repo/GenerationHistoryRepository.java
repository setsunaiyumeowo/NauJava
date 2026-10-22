package ru.KodinMD.NauJava.repo;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.CrudRepository;
import ru.KodinMD.NauJava.entity.GenerationHistory;
import java.util.List;
@RepositoryRestResource(path = "history")
public interface GenerationHistoryRepository extends CrudRepository<GenerationHistory, Long> {
    List<GenerationHistory> findByPasswordIdOrderByIdAsc(Long passwordId);
}
