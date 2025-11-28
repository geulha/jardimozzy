package com.giuliadev.jardimozzy.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;

    @Column(name = "pergunta_seguranca")
    private String perguntaSeguranca;

    private String resposta;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Pet> pets;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Comentario> comentarios;

    public Usuario() {}

    public Usuario(String nome, String email, String senha, String perguntaSeguranca, String resposta) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perguntaSeguranca = perguntaSeguranca;
        this.resposta = resposta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPerguntaSeguranca() {
        return perguntaSeguranca;
    }

    public void setPerguntaSeguranca(String perguntaSeguranca) {
        this.perguntaSeguranca = perguntaSeguranca;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }
}