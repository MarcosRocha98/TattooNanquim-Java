package com.casananquim.dao;

import com.casananquim.database.DatabaseConnection;
import com.casananquim.models.Artista;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistaDAO {
    
    public List<Artista> listarTodos() {
        List<Artista> artistas = new ArrayList<>();
        String sql = "SELECT * FROM artistas";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Artista a = new Artista();
                a.setId(rs.getInt("id"));
                a.setNome(rs.getString("nome"));
                a.setEstilo(rs.getString("estilo"));
                a.setInstagram(rs.getString("instagram"));
                artistas.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artistas;
    }
}