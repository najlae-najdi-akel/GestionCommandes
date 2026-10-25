package ma.fstt.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import ma.fstt.dao.ProduitDAO;
import ma.fstt.entities.Produit;

public class ProduitService {

    private final ProduitDAO produitDAO = new ProduitDAO();

    public List<Produit> listerTous() throws SQLException {
        return produitDAO.listerTous();
    }

    public Produit trouverParId(int id) throws SQLException {
        return produitDAO.trouverParId(id);
    }

    public void ajouter(Produit p) throws SQLException {
        valider(p);
        produitDAO.ajouter(p);
    }

    public void modifier(Produit p) throws SQLException {
        valider(p);
        produitDAO.modifier(p);
    }

    public void supprimer(int id) throws SQLException {
        produitDAO.supprimer(id);
    }

    private void valider(Produit p) {
        if (p.getNom() == null || p.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du produit est obligatoire.");
        }
        if (p.getPrix() == null || p.getPrix().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le prix doit être positif ou nul.");
        }
        if (p.getStock() < 0) {
            throw new IllegalArgumentException("Le stock ne peut pas être négatif.");
        }
        p.setNom(p.getNom().trim());
        if (p.getDescription() != null && p.getDescription().trim().isEmpty()) {
            p.setDescription(null);
        }
    }
}