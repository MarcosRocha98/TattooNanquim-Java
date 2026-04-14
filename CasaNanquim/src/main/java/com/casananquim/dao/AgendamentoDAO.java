package com.casananquim.dao;

import com.casananquim.database.DatabaseConnection;
import com.casananquim.models.Agendamento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoDAO {
    
    public boolean salvar(Agendamento a) {
        String sql = "INSERT INTO agendamentos (cliente_nome, cliente_whatsapp, artista_id, data_agendamento, horario, descricao, local_corpo, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'PENDENTE')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, a.getClienteNome());
            pstmt.setString(2, a.getClienteWhatsapp());
            pstmt.setInt(3, a.getArtistaId());
            pstmt.setString(4, a.getDataAgendamento());
            pstmt.setString(5, a.getHorario());
            pstmt.setString(6, a.getDescricao());
            pstmt.setString(7, a.getLocalCorpo());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Agendamento> listarTodos() {
        List<Agendamento> lista = new ArrayList<>();
        String sql = "SELECT a.*, ar.nome as artista_nome FROM agendamentos a LEFT JOIN artistas ar ON a.artista_id = ar.id ORDER BY a.data_agendamento, a.horario";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Agendamento ag = new Agendamento();
                ag.setId(rs.getInt("id"));
                ag.setClienteNome(rs.getString("cliente_nome"));
                ag.setClienteWhatsapp(rs.getString("cliente_whatsapp"));
                ag.setArtistaId(rs.getInt("artista_id"));
                ag.setArtistaNome(rs.getString("artista_nome"));
                ag.setDataAgendamento(rs.getString("data_agendamento"));
                ag.setHorario(rs.getString("horario"));
                ag.setDescricao(rs.getString("descricao"));
                ag.setLocalCorpo(rs.getString("local_corpo"));
                ag.setStatus(rs.getString("status"));
                lista.add(ag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<String> getHorariosOcupados(String data, int artistaId) {
        List<String> horarios = new ArrayList<>();
        String sql = "SELECT TIME_FORMAT(horario, '%H:%i') as horario_format FROM agendamentos WHERE data_agendamento = ? AND artista_id = ? AND status != 'CANCELADO'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, data);
            pstmt.setInt(2, artistaId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                horarios.add(rs.getString("horario_format"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return horarios;
    }
    
    public boolean atualizarStatus(int id, String status) {
        String sql = "UPDATE agendamentos SET status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletar(int id) {
        String sql = "DELETE FROM agendamentos WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<String> getHorariosConfig() {
        List<String> horarios = new ArrayList<>();
        String sql = "SELECT valor FROM configuracoes WHERE chave = 'HORARIO' ORDER BY valor";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                horarios.add(rs.getString("valor"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return horarios;
    }
    
    public List<String> getDiasFechados() {
        List<String> dias = new ArrayList<>();
        String sql = "SELECT valor FROM configuracoes WHERE chave = 'DIA_FECHADO'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                dias.add(rs.getString("valor"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dias;
    }
}