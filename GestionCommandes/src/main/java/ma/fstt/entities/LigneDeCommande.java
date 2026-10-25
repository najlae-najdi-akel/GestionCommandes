package ma.fstt.entities;

import java.math.BigDecimal;

public class LigneDeCommande {

    private int id;
    private int commandeId;
    private int produitId;
    private int quantite;
    private BigDecimal prixUnitaire;

    // Champ utile pour l'affichage (rempli par une jointure)
    private String produitNom;

    public LigneDeCommande() {
    }

    public LigneDeCommande(int produitId, int quantite, BigDecimal prixUnitaire) {
        this.produitId = produitId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public LigneDeCommande(int id, int commandeId, int produitId, int quantite, BigDecimal prixUnitaire) {
        this.id = id;
        this.commandeId = commandeId;
        this.produitId = produitId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCommandeId() { return commandeId; }
    public void setCommandeId(int commandeId) { this.commandeId = commandeId; }

    public int getProduitId() { return produitId; }
    public void setProduitId(int produitId) { this.produitId = produitId; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public String getProduitNom() { return produitNom; }
    public void setProduitNom(String produitNom) { this.produitNom = produitNom; }

    // Total de la ligne = prix unitaire x quantité
    public BigDecimal getSousTotal() {
        if (prixUnitaire == null) {
            return BigDecimal.ZERO;
        }
        return prixUnitaire.multiply(BigDecimal.valueOf(quantite));
    }
}
