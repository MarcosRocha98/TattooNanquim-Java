package com.casananquim.dao;

import com.casananquim.database.DatabaseConnection;
import com.casananquim.models.Tatuador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TatuadorDAO {
    
    public boolean cadastrar(Tatuador t) {
        String sql = "INSERT INTO tatuadores (nome, email, senha, telefone, instagram, especialidade) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, t.getNome());
            pstmt.setString(2, t.getEmail());
            pstmt.setString(3, t.getSenha());
            pstmt.setString(4, t.getTelefone());
            pstmt.setString(5, t.getInstagram());
            pstmt.setString(6, t.getEspecialidade());
            
            int resultado = pstmt.executeUpdate();
            System.out.println("Cadastro executado, resultado: " + resultado);
            return resultado > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Tatuador buscarPorEmail(String email) {
        String sql = "SELECT * FROM tatuadores WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extrairTatuador(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Tatuador buscarPorId(int id) {
        String sql = "SELECT * FROM tatuadores WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extrairTatuador(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private Tatuador extrairTatuador(ResultSet rs) throws SQLException {
        Tatuador t = new Tatuador();
        t.setId(rs.getInt("id"));
        t.setNome(rs.getString("nome"));
        t.setEmail(rs.getString("email"));
        t.setSenha(rs.getString("senha"));
        t.setTelefone(rs.getString("telefone"));
        t.setInstagram(rs.getString("instagram"));
        t.setEspecialidade(rs.getString("especialidade"));
        t.setContadorSolicitacoes(rs.getInt("contador_solicitacoes"));
        t.setDataCadastro(rs.getString("data_cadastro"));
        t.setUltimoLogin(rs.getString("ultimo_login"));
        t.setAtivo(rs.getBoolean("ativo"));
        return t;
    }
    
    public boolean atualizarUltimoLogin(int id) {
        String sql = "UPDATE tatuadores SET ultimo_login = NOW() WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean incrementarContadorSolicitacoes(int id) {
        String sql = "UPDATE tatuadores SET contador_solicitacoes = contador_solicitacoes + 1 WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Tatuador> listarTodos() {
        List<Tatuador> lista = new ArrayList<>();
        String sql = "SELECT * FROM tatuadores ORDER BY data_cadastro DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(extrairTatuador(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}