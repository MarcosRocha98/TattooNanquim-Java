package com.casananquim.models;

public class PrecoEspaco {
    private int id;
    private String periodo;
    private double valor;
    private String descricao;
    
    public PrecoEspaco() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}