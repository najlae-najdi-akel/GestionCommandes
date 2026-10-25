package ma.fstt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ma.fstt.entities.Commande;
import ma.fstt.entities.LigneDeCommande;

public class CommandeDAO {

    // CREATE : enregistre la commande + ses lignes + met à jour le stock (transaction)
    public void creer(Commande commande) throws SQLException {
        Connection cn = null;
        try {
            cn = DBConnection.getConnection();
            cn.setAutoCommit(false); // début de la transaction

            // 1. Insérer la commande
            String sqlCmd = "INSERT INTO commandes (statut, client_id) VALUES (?, ?)";
            try (PreparedStatement ps = cn.prepareStatement(sqlCmd, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, commande.getStatut() != null ? commande.getStatut() : "En attente");
                ps.setInt(2, commande.getClientId());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        commande.setId(rs.getInt(1));
                    }
                }
            }

            // 2. Pour chaque ligne : vérifier le stock, insérer la ligne, décrémenter le stock
            String sqlStock = "SELECT stock FROM produits WHERE id = ? FOR UPDATE";
            String sqlLigne = "INSERT INTO lignes_commande (commande_id, produit_id, quantite, prix_unitaire) VALUES (?, ?, ?, ?)";
            String sqlMajStock = "UPDATE produits SET stock = stock - ? WHERE id = ?";

            for (LigneDeCommande l : commande.getLignes()) {

                // Vérification du stock (FOR UPDATE verrouille la ligne pendant la transaction)
                try (PreparedStatement ps = cn.prepareStatement(sqlStock)) {
                    ps.setInt(1, l.getProduitId());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Produit introuvable (id " + l.getProduitId() + ")");
                        }
                        if (rs.getInt("stock") < l.getQuantite()) {
                            throw new SQLException("Stock insuffisant pour le produit id " + l.getProduitId());
                        }
                    }
                }

                // Insertion de la ligne
                try (PreparedStatement ps = cn.prepareStatement(sqlLigne)) {
                    ps.setInt(1, commande.getId());
                    ps.setInt(2, l.getProduitId());
                    ps.setInt(3, l.getQuantite());
                    ps.setBigDecimal(4, l.getPrixUnitaire());
                    ps.executeUpdate();
                }

                // Décrémentation du stock
                try (PreparedStatement ps = cn.prepareStatement(sqlMajStock)) {
                    ps.setInt(1, l.getQuantite());
                    ps.setInt(2, l.getProduitId());
                    ps.executeUpdate();
                }
            }

            cn.commit(); // tout s'est bien passé : on valide

        } catch (SQLException e) {
            if (cn != null) {
                cn.rollback(); // erreur : on annule tout
            }
            throw e;
        } finally {
            if (cn != null) {
                cn.setAutoCommit(true);
                cn.close();
            }
        }
    }

    // READ : toutes les commandes avec le nom du client (sans les lignes)
    public List<Commande> listerToutes() throws SQLException {
        List<Commande> liste = new ArrayList<>();
        String sql = "SELECT c.id, c.date_commande, c.statut, c.client_id, cl.nom AS client_nom "
                   + "FROM commandes c JOIN clients cl ON c.client_id = cl.id "
                   + "ORDER BY c.date_commande DESC";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Commande c = new Commande(
                        rs.getInt("id"),
                        rs.getTimestamp("date_commande"),
                        rs.getString("statut"),
                        rs.getInt("client_id"));
                c.setClientNom(rs.getString("client_nom"));
                liste.add(c);
            }
        }
        return liste;
    }

    // READ : une commande avec son client et toutes ses lignes (null si introuvable)
    public Commande trouverParId(int id) throws SQLException {
        Commande commande = null;
        String sqlCmd = "SELECT c.id, c.date_commande, c.statut, c.client_id, cl.nom AS client_nom "
                      + "FROM commandes c JOIN clients cl ON c.client_id = cl.id WHERE c.id = ?";
        String sqlLignes = "SELECT l.id, l.commande_id, l.produit_id, l.quantite, l.prix_unitaire, p.nom AS produit_nom "
                         + "FROM lignes_commande l JOIN produits p ON l.produit_id = p.id "
                         + "WHERE l.commande_id = ?";
        try (Connection cn = DBConnection.getConnection()) {
            try (PreparedStatement ps = cn.prepareStatement(sqlCmd)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        commande = new Commande(
                                rs.getInt("id"),
                                rs.getTimestamp("date_commande"),
                                rs.getString("statut"),
                                rs.getInt("client_id"));
                        commande.setClientNom(rs.getString("client_nom"));
                    }
                }
            }
            if (commande != null) {
                try (PreparedStatement ps = cn.prepareStatement(sqlLignes)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            LigneDeCommande l = new LigneDeCommande(
                                    rs.getInt("id"),
                                    rs.getInt("commande_id"),
                                    rs.getInt("produit_id"),
                                    rs.getInt("quantite"),
                                    rs.getBigDecimal("prix_unitaire"));
                            l.setProduitNom(rs.getString("produit_nom"));
                            commande.getLignes().add(l);
                        }
                    }
                }
            }
        }
        return commande;
    }

    // UPDATE : change uniquement le statut d'une commande
    public void modifierStatut(int id, String statut) throws SQLException {
        String sql = "UPDATE commandes SET statut = ? WHERE id = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
}
