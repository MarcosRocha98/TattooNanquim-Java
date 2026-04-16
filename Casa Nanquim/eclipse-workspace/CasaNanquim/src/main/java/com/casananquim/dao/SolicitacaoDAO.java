package com.casananquim.dao;

import com.casananquim.database.DatabaseConnection;
import com.casananquim.models.Solicitacao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolicitacaoDAO {
    
    public boolean salvar(Solicitacao s) {
        String sql = "INSERT INTO solicitacoes (tatuador_id, data_solicitacao, horario_inicio, horario_fim, periodo_escolhido, valor, observacao, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'PENDENTE')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, s.getTatuadorId());
            pstmt.setString(2, s.getDataSolicitacao());
            pstmt.setString(3, s.getHorarioInicio());
            pstmt.setString(4, s.getHorarioFim());
            pstmt.setString(5, s.getPeriodoEscolhido());
            pstmt.setDouble(6, s.getValor());
            pstmt.setString(7, s.getObservacao());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Solicitacao> listarPorTatuador(int tatuadorId) {
        List<Solicitacao> lista = new ArrayList<>();
        String sql = "SELECT s.*, t.nome as tatuador_nome FROM solicitacoes s LEFT JOIN tatuadores t ON s.tatuador_id = t.id WHERE s.tatuador_id = ? ORDER BY s.data_solicitacao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, tatuadorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                lista.add(extrairSolicitacao(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<Solicitacao> listarTodas() {
        List<Solicitacao> lista = new ArrayList<>();
        String sql = "SELECT s.*, t.nome as tatuador_nome FROM solicitacoes s LEFT JOIN tatuadores t ON s.tatuador_id = t.id ORDER BY s.data_solicitacao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(extrairSolicitacao(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    private Solicitacao extrairSolicitacao(ResultSet rs) throws SQLException {
        Solicitacao s = new Solicitacao();
        s.setId(rs.getInt("id"));
        s.setTatuadorId(rs.getInt("tatuador_id"));
        s.setTatuadorNome(rs.getString("tatuador_nome"));
        s.setDataSolicitacao(rs.getString("data_solicitacao"));
        s.setHorarioInicio(rs.getString("horario_inicio"));
        s.setHorarioFim(rs.getString("horario_fim"));
        s.setPeriodoEscolhido(rs.getString("periodo_escolhido"));
        s.setValor(rs.getDouble("valor"));
        s.setStatus(rs.getString("status"));
        s.setObservacao(rs.getString("observacao"));
        s.setCriadoEm(rs.getString("criado_em"));
        return s;
    }
    
    public List<String> getHorariosOcupados(String data) {
        List<String> horarios = new ArrayList<>();
        String sql = "SELECT TIME_FORMAT(horario_inicio, '%H:%i') as hora_inicio FROM solicitacoes WHERE data_solicitacao = ? AND status != 'CANCELADO'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, data);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String horario = rs.getString("hora_inicio");
                if (horario != null) {
                    horarios.add(horario);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return horarios;
    }
    
    public boolean atualizarStatus(int id, String status) {
        String sql = "UPDATE solicitacoes SET status = ? WHERE id = ?";
        
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
        String sql = "DELETE FROM solicitacoes WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}