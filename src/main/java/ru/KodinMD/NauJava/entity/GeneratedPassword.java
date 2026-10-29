package ru.KodinMD.NauJava.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "generated_passwords")
public class GeneratedPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,length = 100)
    private String label;
    @Column(name = "secret_value",nullable = false,length = 512)
    private String secretValue;
    @Column(nullable = false)
    private int length;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
    @ManyToOne
    @JoinColumn(name = "algorithm_id",nullable = false)
    private GenerationAlgorithm algorithm;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private PasswordCategory category;
    @OneToMany(mappedBy = "password", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<GenerationHistory> history = new ArrayList<>();
    public Long getId(){
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setSecretValue(String secretValue) {
        this.secretValue = secretValue;
    }
    public String getSecretValue() {
        return secretValue;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public int getLength() {
        return length;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
    public AppUser getUser() {
        return user;
    }
    public void setUser(AppUser user) {
        this.user = user;
    }
    public GenerationAlgorithm getAlgorithm() {
        return algorithm;
    }
    public void setAlgorithm(GenerationAlgorithm algorithm) {
        this.algorithm = algorithm;
    }
    public PasswordCategory getCategory() {
        return category;
    }
    public void setCategory(PasswordCategory category) {
        this.category = category;
    }
    public List<GenerationHistory> getHistory() {
        return history;
    }
    public void setHistory(List<GenerationHistory> history) {
        this.history = history;
    }
}