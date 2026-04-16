package com.casananquim.models;

public class Tatuador {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String instagram;
    private String especialidade;
    private int contadorSolicitacoes;
    private String dataCadastro;
    private String ultimoLogin;
    private boolean ativo;
    
    public Tatuador() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getInstagram() { return instagram; }
    public void setInstagram(String instagram) { this.instagram = instagram; }
    
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    
    public int getContadorSolicitacoes() { return contadorSolicitacoes; }
    public void setContadorSolicitacoes(int contadorSolicitacoes) { this.contadorSolicitacoes = contadorSolicitacoes; }
    
    public String getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro; }
    
    public String getUltimoLogin() { return ultimoLogin; }
    public void setUltimoLogin(String ultimoLogin) { this.ultimoLogin = ultimoLogin; }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}