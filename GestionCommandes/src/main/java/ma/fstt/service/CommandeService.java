package ma.fstt.service;

import java.sql.SQLException;
import java.util.List;

import ma.fstt.dao.ClientDAO;
import ma.fstt.dao.CommandeDAO;
import ma.fstt.dao.ProduitDAO;
import ma.fstt.entities.Commande;
import ma.fstt.entities.LigneDeCommande;
import ma.fstt.entities.Produit;

public class CommandeService {

    private final CommandeDAO commandeDAO = new CommandeDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final ProduitDAO produitDAO = new ProduitDAO();

    public List<Commande> listerToutes() throws SQLException {
        return commandeDAO.listerToutes();
    }

    public Commande trouverParId(int id) throws SQLException {
        return commandeDAO.trouverParId(id);
    }

    public void modifierStatut(int id, String statut) throws SQLException {
        if (statut == null || statut.trim().isEmpty()) {
            throw new IllegalArgumentException("Le statut est obligatoire.");
        }
        commandeDAO.modifierStatut(id, statut.trim());
    }

    // Crée une commande après vérification des règles de gestion
    public void creer(Commande commande) throws SQLException {
        if (clientDAO.trouverParId(commande.getClientId()) == null) {
            throw new IllegalArgumentException("Client introuvable.");
        }
        if (commande.getLignes() == null || commande.getLignes().isEmpty()) {
            throw new IllegalArgumentException("La commande doit contenir au moins une ligne.");
        }
        for (LigneDeCommande l : commande.getLignes()) {
            if (l.getQuantite() <= 0) {
                throw new IllegalArgumentException("La quantité doit être supérieure à 0.");
            }
            Produit p = produitDAO.trouverParId(l.getProduitId());
            if (p == null) {
                throw new IllegalArgumentException("Produit introuvable (id " + l.getProduitId() + ").");
            }
            // Le prix vient toujours de la base, jamais du formulaire
            l.setPrixUnitaire(p.getPrix());
        }
        commandeDAO.creer(commande);
    }
}