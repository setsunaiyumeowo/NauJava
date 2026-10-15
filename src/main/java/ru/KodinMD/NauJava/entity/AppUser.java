package ru.KodinMD.NauJava.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 50)
    private String login;
    @Column(nullable = false, length = 100)
    private String email;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GeneratedPassword> passwords = new ArrayList<>();
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getLogin(){
        return login;
    }
    public void setLogin(String login){
        this.login = login;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public LocalDateTime  getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    public List<GeneratedPassword> getPasswords(){
        return passwords;
    }

    public void setPasswords(List<GeneratedPassword> passwords){
        this.passwords = passwords;
    }
}
