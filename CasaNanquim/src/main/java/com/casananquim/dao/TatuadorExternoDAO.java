package com.casananquim.dao;

import com.casananquim.database.DatabaseConnection;
import com.casananquim.models.TatuadorExterno;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TatuadorExternoDAO {
    
    public boolean salvar(TatuadorExterno t) {
        String sql = "INSERT INTO tatuadores_externos (nome, whatsapp, email, instagram, especialidade, ativo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, t.getNome());
            pstmt.setString(2, t.getWhatsapp());
            pstmt.setString(3, t.getEmail());
            pstmt.setString(4, t.getInstagram());
            pstmt.setString(5, t.getEspecialidade());
            pstmt.setBoolean(6, true);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public TatuadorExterno buscarPorWhatsapp(String whatsapp) {
        String sql = "SELECT * FROM tatuadores_externos WHERE whatsapp = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, whatsapp);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                TatuadorExterno t = new TatuadorExterno();
                t.setId(rs.getInt("id"));
                t.setNome(rs.getString("nome"));
                t.setWhatsapp(rs.getString("whatsapp"));
                t.setEmail(rs.getString("email"));
                t.setInstagram(rs.getString("instagram"));
                t.setEspecialidade(rs.getString("especialidade"));
                return t;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}