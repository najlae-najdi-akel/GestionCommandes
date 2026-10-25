package ma.fstt.dao;

import java.math.BigDecimal;
import java.sql.SQLException;

import ma.fstt.entities.Produit;

public class TestProduitDAO {

    public static void main(String[] args) {
        ProduitDAO dao = new ProduitDAO();
        try {
            // 1. Ajout
            Produit p = new Produit("Clavier Test", "Clavier USB", new BigDecimal("199.90"), 10);
            dao.ajouter(p);
            System.out.println("Ajouté, id = " + p.getId());

            // 2. Modification
            p.setPrix(new BigDecimal("149.50"));
            dao.modifier(p);
            System.out.println("Après modification, prix = " + dao.trouverParId(p.getId()).getPrix());

            // 3. Modification du stock
            dao.modifierStock(p.getId(), 7);
            System.out.println("Nouveau stock = " + dao.trouverParId(p.getId()).getStock());

            // 4. Liste
            System.out.println("--- Liste des produits ---");
            for (Produit x : dao.listerTous()) {
                System.out.println(x.getId() + " | " + x.getNom() + " | " + x.getPrix() + " | stock " + x.getStock());
            }

            // 5. Suppression
            dao.supprimer(p.getId());
            System.out.println("Supprimé : " + (dao.trouverParId(p.getId()) == null));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
