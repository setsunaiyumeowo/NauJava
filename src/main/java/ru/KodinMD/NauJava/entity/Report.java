package ru.KodinMD.NauJava.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReportStatus status;
    @Column(columnDefinition = "TEXT")
    private String content;
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public ReportStatus getStatus(){
        return status;
    }
    public void setStatus(ReportStatus status){
        this.status = status;
    }
    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }
}
