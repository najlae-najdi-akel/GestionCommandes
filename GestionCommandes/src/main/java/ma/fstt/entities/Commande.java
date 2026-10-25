package ma.fstt.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Commande {

    private int id;
    private Timestamp dateCommande;
    private String statut;
    private int clientId;

    // Champs utiles pour l'affichage (remplis par des jointures)
    private String clientNom;
    private List<LigneDeCommande> lignes = new ArrayList<>();

    public Commande() {
    }

    public Commande(int clientId, String statut) {
        this.clientId = clientId;
        this.statut = statut;
    }

    public Commande(int id, Timestamp dateCommande, String statut, int clientId) {
        this.id = id;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.clientId = clientId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Timestamp getDateCommande() { return dateCommande; }
    public void setDateCommande(Timestamp dateCommande) { this.dateCommande = dateCommande; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public String getClientNom() { return clientNom; }
    public void setClientNom(String clientNom) { this.clientNom = clientNom; }

    public List<LigneDeCommande> getLignes() { return lignes; }
    public void setLignes(List<LigneDeCommande> lignes) { this.lignes = lignes; }
}