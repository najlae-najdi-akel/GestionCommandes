package ma.fstt.service;

import java.sql.SQLException;
import java.util.List;

import ma.fstt.dao.ClientDAO;
import ma.fstt.entities.Client;

public class ClientService {

    private final ClientDAO clientDAO = new ClientDAO();

    public List<Client> listerTous() throws SQLException {
        return clientDAO.listerTous();
    }

    public Client trouverParId(int id) throws SQLException {
        return clientDAO.trouverParId(id);
    }

    public void ajouter(Client c) throws SQLException {
        valider(c);
        clientDAO.ajouter(c);
    }

    public void modifier(Client c) throws SQLException {
        valider(c);
        clientDAO.modifier(c);
    }

    public void supprimer(int id) throws SQLException {
        clientDAO.supprimer(id);
    }

    // Règles de gestion : lève une IllegalArgumentException si les données sont invalides
    private void valider(Client c) {
        if (c.getNom() == null || c.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du client est obligatoire.");
        }
        if (c.getEmail() == null || !c.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("L'email du client est invalide.");
        }
        c.setNom(c.getNom().trim());
        c.setEmail(c.getEmail().trim());
        if (c.getTelephone() != null && c.getTelephone().trim().isEmpty()) {
            c.setTelephone(null); // la colonne accepte NULL
        }
    }
}
