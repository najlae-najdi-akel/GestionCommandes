package ma.fstt.dao;

import java.sql.SQLException;

import ma.fstt.entities.Client;

public class TestClientDAO {

    public static void main(String[] args) {
        ClientDAO dao = new ClientDAO();
        try {
            // 1. Ajout
            Client c = new Client("Test Client", "test@mail.com", "0600000000");
            dao.ajouter(c);
            System.out.println("Ajouté, id = " + c.getId());

            // 2. Modification
            c.setNom("Test Client Modifié");
            dao.modifier(c);
            System.out.println("Après modification : " + dao.trouverParId(c.getId()).getNom());

            // 3. Liste
            System.out.println("--- Liste des clients ---");
            for (Client x : dao.listerTous()) {
                System.out.println(x.getId() + " | " + x.getNom() + " | " + x.getEmail());
            }

            // 4. Suppression
            dao.supprimer(c.getId());
            System.out.println("Supprimé : " + (dao.trouverParId(c.getId()) == null));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}