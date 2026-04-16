package com.casananquim.dao;

import com.casananquim.database.DatabaseConnection;
import com.casananquim.models.PrecoEspaco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrecoEspacoDAO {
    
    public List<PrecoEspaco> listarTodos() {
        List<PrecoEspaco> lista = new ArrayList<>();
        String sql = "SELECT * FROM precos_espaco";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                PrecoEspaco p = new PrecoEspaco();
                p.setId(rs.getInt("id"));
                p.setPeriodo(rs.getString("periodo"));
                p.setValor(rs.getDouble("valor"));
                p.setDescricao(rs.getString("descricao"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}