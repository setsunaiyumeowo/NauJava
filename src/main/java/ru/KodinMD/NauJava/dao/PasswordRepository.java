package ru.KodinMD.NauJava.dao;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.KodinMD.NauJava.entity.GeneratedPassword;
@Component
public class PasswordRepository implements CrudRepository<GeneratedPassword, Long> {
    private final List<GeneratedPassword> passwordContainer;
    @Autowired
    public PasswordRepository(List<GeneratedPassword> passwordContainer) {
        this.passwordContainer = passwordContainer;
    }

    @Override
    public void create(GeneratedPassword entity) {
        passwordContainer.add(entity);
    }

    @Override
    public GeneratedPassword read(Long id) {
        return passwordContainer.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void update(GeneratedPassword entity) {
        for (int i = 0; i < passwordContainer.size(); i++){
            if (passwordContainer.get(i).getId().equals(entity.getId())){
                passwordContainer.set(i,entity);
                return;
            }
        }

    }

    @Override
    public void delete(Long id) {
        passwordContainer.removeIf(p -> p.getId().equals(id));
    }

    @Override
    public List<GeneratedPassword> findAll() {
        return new ArrayList<>(passwordContainer);
    }

}
