package ru.KodinMD.NauJava.repo;

import org.springframework.data.repository.CrudRepository;
import ru.KodinMD.NauJava.entity.GenerationAlgorithm;
import java.util.Optional;

public interface GenerationAlgorithmRepository extends CrudRepository<GenerationAlgorithm, Long> {
    Optional<GenerationAlgorithm> findByName(String name);
}
