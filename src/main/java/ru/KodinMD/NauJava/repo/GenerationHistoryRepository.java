package ru.KodinMD.NauJava.repo;

import org.springframework.data.repository.CrudRepository;
import ru.KodinMD.NauJava.entity.GenerationHistory;
import java.util.List;

public interface GenerationHistoryRepository extends CrudRepository<GenerationHistory, Long> {
    List<GenerationHistory> findByPasswordIdOrderByIdAsc(Long passwordId);
}
