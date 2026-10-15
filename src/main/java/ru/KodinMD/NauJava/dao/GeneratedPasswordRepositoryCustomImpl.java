package ru.KodinMD.NauJava.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.KodinMD.NauJava.entity.AppUser;
import ru.KodinMD.NauJava.entity.GeneratedPassword;
import java.util.List;
@Repository
public class GeneratedPasswordRepositoryCustomImpl implements GeneratedPasswordRepositoryCustom{
    private final EntityManager entityManager;
    @Autowired
    public GeneratedPasswordRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public List<GeneratedPassword> findByLengthBetweenAndLabel(int minLength, int maxLength, String label) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GeneratedPassword> criteriaQuery = criteriaBuilder.createQuery(GeneratedPassword.class);
        Root<GeneratedPassword> passwordRoot = criteriaQuery.from(GeneratedPassword.class);
        Predicate lengthPredicate = criteriaBuilder.between(
                passwordRoot.<Integer>get("length"), minLength, maxLength);
        Predicate labelPredicate = criteriaBuilder.equal(passwordRoot.get("label"), label);
        criteriaQuery.select(passwordRoot).where(criteriaBuilder.and(lengthPredicate, labelPredicate));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
    @Override
    public List<GeneratedPassword> findByUserLogin(String login) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GeneratedPassword> criteriaQuery = criteriaBuilder.createQuery(GeneratedPassword.class);
        Root<GeneratedPassword> passwordRoot = criteriaQuery.from(GeneratedPassword.class);
        Join<GeneratedPassword, AppUser> user = passwordRoot.join("user", JoinType.INNER);
        Predicate loginPredicate = criteriaBuilder.equal(user.get("login"), login);
        criteriaQuery.select(passwordRoot).where(loginPredicate);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
