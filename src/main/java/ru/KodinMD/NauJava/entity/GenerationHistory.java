package ru.KodinMD.NauJava.entity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Table(name = "generation_history")
public class GenerationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private HistoryAction action;
    @Column(nullable = false)
    private int length;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "password_id", nullable = false)
    private GeneratedPassword password;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public HistoryAction getAction() {
        return action;
    }
    public void setAction(HistoryAction action) {
        this.action = action;
    }
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public GeneratedPassword getPassword() {
        return password;
    }
    public void setPassword(GeneratedPassword password) {
        this.password = password;
    }
}
