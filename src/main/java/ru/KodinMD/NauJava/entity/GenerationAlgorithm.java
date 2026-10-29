package ru.KodinMD.NauJava.entity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "generation_algorithms")
public class GenerationAlgorithm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true,length = 100)
    private String name;
    @Column(length = 255)
    private String description;
    @Column(nullable = false,length = 255)
    private String alphabet;
    @OneToMany(mappedBy = "algorithm")
    private List<GeneratedPassword> passwords = new ArrayList<>();
    public Long getId(){
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getAlphabet() {
        return alphabet;
    }
    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }
    public List<GeneratedPassword> getPasswords() {
        return passwords;
    }
    public void setPasswords(List<GeneratedPassword> passwords){
        this.passwords = passwords;
    }
}
