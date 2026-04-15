package com.casananquim.models;

public class ReservaEspaco {
    private int id;
    private int tatuadorId;
    private String tatuadorNome;
    private String tatuadorWhatsapp;
    private String email;
    private String instagram;
    private String especialidade;
    private String dataReserva;
    private String horarioInicio;
    private String horarioFim;
    private double valor;
    private String status;
    private String observacao;
    
    public ReservaEspaco() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getTatuadorId() { return tatuadorId; }
    public void setTatuadorId(int tatuadorId) { this.tatuadorId = tatuadorId; }
    
    public String getTatuadorNome() { return tatuadorNome; }
    public void setTatuadorNome(String tatuadorNome) { this.tatuadorNome = tatuadorNome; }
    
    public String getTatuadorWhatsapp() { return tatuadorWhatsapp; }
    public void setTatuadorWhatsapp(String tatuadorWhatsapp) { this.tatuadorWhatsapp = tatuadorWhatsapp; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getInstagram() { return instagram; }
    public void setInstagram(String instagram) { this.instagram = instagram; }
    
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    
    public String getDataReserva() { return dataReserva; }
    public void setDataReserva(String dataReserva) { this.dataReserva = dataReserva; }
    
    public String getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(String horarioInicio) { this.horarioInicio = horarioInicio; }
    
    public String getHorarioFim() { return horarioFim; }
    public void setHorarioFim(String horarioFim) { this.horarioFim = horarioFim; }
    
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}