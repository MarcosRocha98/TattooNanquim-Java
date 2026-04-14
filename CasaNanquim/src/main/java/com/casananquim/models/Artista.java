package com.casananquim.models;

public class Artista {
    private int id;
    private String nome;
    private String estilo;
    private String instagram;
    
    public Artista() {}
    
    public Artista(int id, String nome, String estilo, String instagram) {
        this.id = id;
        this.nome = nome;
        this.estilo = estilo;
        this.instagram = instagram;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEstilo() { return estilo; }
    public void setEstilo(String estilo) { this.estilo = estilo; }
    public String getInstagram() { return instagram; }
    public void setInstagram(String instagram) { this.instagram = instagram; }
}