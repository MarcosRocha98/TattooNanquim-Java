package com.casananquim.dao;

import com.casananquim.database.DatabaseConnection;
import com.casananquim.models.ReservaEspaco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaEspacoDAO {
    
    public boolean salvar(ReservaEspaco r) {
        String sql = "INSERT INTO reservas_espaco (tatuador_id, tatuador_nome, tatuador_whatsapp, data_reserva, horario_inicio, horario_fim, valor, status, observacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, r.getTatuadorId());
            pstmt.setString(2, r.getTatuadorNome());
            pstmt.setString(3, r.getTatuadorNome()); // whatsapp
            pstmt.setString(4, r.getDataReserva());
            pstmt.setString(5, r.getHorarioInicio());
            pstmt.setString(6, r.getHorarioFim());
            pstmt.setDouble(7, r.getValor());
            pstmt.setString(8, "PENDENTE");
            pstmt.setString(9, r.getObservacao());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<ReservaEspaco> listarTodos() {
        List<ReservaEspaco> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas_espaco ORDER BY data_reserva, horario_inicio";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ReservaEspaco r = new ReservaEspaco();
                r.setId(rs.getInt("id"));
                r.setTatuadorId(rs.getInt("tatuador_id"));
                r.setTatuadorNome(rs.getString("tatuador_nome"));
                r.setDataReserva(rs.getString("data_reserva"));
                r.setHorarioInicio(rs.getString("horario_inicio"));
                r.setHorarioFim(rs.getString("horario_fim"));
                r.setValor(rs.getDouble("valor"));
                r.setStatus(rs.getString("status"));
                r.setObservacao(rs.getString("observacao"));
                lista.add(r);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
    
    public List<String> getHorariosOcupados(String data) {
        List<String> horarios = new ArrayList<>();
        String sql = "SELECT horario_inicio FROM reservas_espaco WHERE data_reserva = ? AND status != 'CANCELADO'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, data);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                horarios.add(rs.getString("horario_inicio"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return horarios;
    }
    
    public boolean atualizarStatus(int id, String status) {
        String sql = "UPDATE reservas_espaco SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    public boolean deletar(int id) {
        String sql = "DELETE FROM reservas_espaco WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}