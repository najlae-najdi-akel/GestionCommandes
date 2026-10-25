package ma.fstt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ma.fstt.entities.Produit;

public class ProduitDAO {

    // CREATE
    public void ajouter(Produit p) throws SQLException {
        String sql = "INSERT INTO produits (nom, description, prix, stock) VALUES (?, ?, ?, ?)";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setBigDecimal(3, p.getPrix());
            ps.setInt(4, p.getStock());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }
        }
    }

    // READ : tous les produits
    public List<Produit> listerTous() throws SQLException {
        List<Produit> liste = new ArrayList<>();
        String sql = "SELECT id, nom, description, prix, stock FROM produits ORDER BY nom";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(mapper(rs));
            }
        }
        return liste;
    }

    // READ : un produit par son id (null s'il n'existe pas)
    public Produit trouverParId(int id) throws SQLException {
        String sql = "SELECT id, nom, description, prix, stock FROM produits WHERE id = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapper(rs);
                }
            }
        }
        return null;
    }

    // UPDATE
    public void modifier(Produit p) throws SQLException {
        String sql = "UPDATE produits SET nom = ?, description = ?, prix = ?, stock = ? WHERE id = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setBigDecimal(3, p.getPrix());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getId());
            ps.executeUpdate();
        }
    }

    // UPDATE : change uniquement le stock (utile lors d'une commande)
    public void modifierStock(int id, int nouveauStock) throws SQLException {
        String sql = "UPDATE produits SET stock = ? WHERE id = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, nouveauStock);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    // DELETE
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM produits WHERE id = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Transforme une ligne du ResultSet en objet Produit
    private Produit mapper(ResultSet rs) throws SQLException {
        return new Produit(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("description"),
                rs.getBigDecimal("prix"),
                rs.getInt("stock"));
    }
}