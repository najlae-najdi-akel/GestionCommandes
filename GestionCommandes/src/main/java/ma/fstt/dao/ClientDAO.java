package ma.fstt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ma.fstt.entities.Client;

public class ClientDAO {

    // CREATE : ajoute un client et récupère l'id généré par MySQL
    public void ajouter(Client c) throws SQLException {
        String sql = "INSERT INTO clients (nom, email, telephone) VALUES (?, ?, ?)";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNom());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getTelephone());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setId(rs.getInt(1));
                }
            }
        }
    }

    // READ : tous les clients
    public List<Client> listerTous() throws SQLException {
        List<Client> liste = new ArrayList<>();
        String sql = "SELECT id, nom, email, telephone FROM clients ORDER BY nom";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("telephone")));
            }
        }
        return liste;
    }

    // READ : un client par son id (null s'il n'existe pas)
    public Client trouverParId(int id) throws SQLException {
        String sql = "SELECT id, nom, email, telephone FROM clients WHERE id = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getString("telephone"));
                }
            }
        }
        return null;
    }

    // UPDATE : modifie un client existant
    public void modifier(Client c) throws SQLException {
        String sql = "UPDATE clients SET nom = ?, email = ?, telephone = ? WHERE id = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getNom());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getTelephone());
            ps.setInt(4, c.getId());
            ps.executeUpdate();
        }
    }

    // DELETE : supprime un client
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}