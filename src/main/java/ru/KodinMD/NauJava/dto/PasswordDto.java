package ru.KodinMD.NauJava.dto;
import ru.KodinMD.NauJava.entity.GeneratedPassword;
import java.time.LocalDateTime;
public record PasswordDto(Long id, String label, int length, LocalDateTime createdAt, String userLogin, String algorithmName, String categoryTitle) {
    public static PasswordDto from(GeneratedPassword p) {
        return new PasswordDto(
                p.getId(),
                p.getLabel(),
                p.getLength(),
                p.getCreatedAt(),
                p.getUser().getLogin(),
                p.getAlgorithm().getName(),
                p.getCategory() == null ? null : p.getCategory().getTitle());
    }
}
