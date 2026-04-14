package com.casananquim.models;

public class Agendamento {
    private int id;
    private String clienteNome;
    private String clienteWhatsapp;
    private int artistaId;
    private String artistaNome;
    private String dataAgendamento;
    private String horario;
    private String descricao;
    private String localCorpo;
    private String status;
    
    public Agendamento() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }
    
    public String getClienteWhatsapp() { return clienteWhatsapp; }
    public void setClienteWhatsapp(String clienteWhatsapp) { this.clienteWhatsapp = clienteWhatsapp; }
    
    public int getArtistaId() { return artistaId; }
    public void setArtistaId(int artistaId) { this.artistaId = artistaId; }
    
    public String getArtistaNome() { return artistaNome; }
    public void setArtistaNome(String artistaNome) { this.artistaNome = artistaNome; }
    
    public String getDataAgendamento() { return dataAgendamento; }
    public void setDataAgendamento(String dataAgendamento) { this.dataAgendamento = dataAgendamento; }
    
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getLocalCorpo() { return localCorpo; }
    public void setLocalCorpo(String localCorpo) { this.localCorpo = localCorpo; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}