package com.giuliadev.jardimozzy.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentario")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    @Column(name = "data_comentario", nullable = false)
    private LocalDateTime dataComentario = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    public Comentario() {}

    public Comentario(String conteudo, Usuario usuario, Pet pet) {
        this.conteudo = conteudo;
        this.usuario = usuario;
        this.pet = pet;
        this.dataComentario = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getConteudo() { return conteudo; }
    public LocalDateTime getDataComentario() { return dataComentario; }
    public Usuario getUsuario() { return usuario; }
    public Pet getPet() { return pet; }

    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setPet(Pet pet) { this.pet = pet; }
}
