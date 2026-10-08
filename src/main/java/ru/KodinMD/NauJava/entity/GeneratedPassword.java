package ru.KodinMD.NauJava.entity;
import java.time.LocalDateTime;

public class GeneratedPassword {
    private Long id;
    private String value;
    private int length;
    private PasswordAlgorithm algorithm;
    private LocalDateTime createdAt;

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getValue(){
        return value;
    }
    public void setValue(String value){
        this.value = value;
    }
     public int getLength(){
        return length;
     }
     public void setLength(int length){
        this.length = length;
     }
     public PasswordAlgorithm getAlgorithm(){
        return algorithm;
     }
     public void setAlgorithm(PasswordAlgorithm algorithm){
        this.algorithm = algorithm;
     }
     public LocalDateTime getCreatedAt(){
        return createdAt;
     }
     public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
     }
}