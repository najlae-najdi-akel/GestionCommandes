package ma.fstt.dao;

import java.math.BigDecimal;
import java.sql.SQLException;

import ma.fstt.entities.Client;
import ma.fstt.entities.Commande;
import ma.fstt.entities.LigneDeCommande;
import ma.fstt.entities.Produit;

public class TestCommandeDAO {

    public static void main(String[] args) {
        ClientDAO clientDAO = new ClientDAO();
        ProduitDAO produitDAO = new ProduitDAO();
        CommandeDAO commandeDAO = new CommandeDAO();

        try {
            // Préparation : un client et un produit (stock = 10)
            Client client = new Client("Client Commande", "cmd@mail.com", "0611111111");
            clientDAO.ajouter(client);
            Produit produit = new Produit("Souris Test", "Souris sans fil", new BigDecimal("50.00"), 10);
            produitDAO.ajouter(produit);

            // 1. Commande valide : 3 souris
            Commande cmd = new Commande(client.getId(), "En attente");
            cmd.getLignes().add(new LigneDeCommande(produit.getId(), 3, produit.getPrix()));
            commandeDAO.creer(cmd);
            System.out.println("Commande créée, id = " + cmd.getId());

            // 2. Vérification du stock : 10 - 3 = 7
            System.out.println("Stock après commande = " + produitDAO.trouverParId(produit.getId()).getStock() + " (attendu : 7)");

            // 3. Relecture de la commande avec ses lignes
            Commande lue = commandeDAO.trouverParId(cmd.getId());
            System.out.println("Client : " + lue.getClientNom() + " | statut : " + lue.getStatut());
            for (LigneDeCommande l : lue.getLignes()) {
                System.out.println("  - " + l.getProduitNom() + " x" + l.getQuantite()
                        + " à " + l.getPrixUnitaire() + " = " + l.getSousTotal());
            }

            // 4. Changement de statut
            commandeDAO.modifierStatut(cmd.getId(), "Validée");
            System.out.println("Nouveau statut : " + commandeDAO.trouverParId(cmd.getId()).getStatut());

            // 5. Commande refusée : 100 souris alors qu'il en reste 7
            Commande trop = new Commande(client.getId(), "En attente");
            trop.getLignes().add(new LigneDeCommande(produit.getId(), 100, produit.getPrix()));
            try {
                commandeDAO.creer(trop);
                System.out.println("ERREUR : la commande aurait dû être refusée !");
            } catch (SQLException e) {
                System.out.println("Refusée comme prévu : " + e.getMessage());
            }
            System.out.println("Stock inchangé = " + produitDAO.trouverParId(produit.getId()).getStock() + " (attendu : 7)");
            System.out.println("Nombre de commandes en base = " + commandeDAO.listerToutes().size() + " (attendu : 1)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}