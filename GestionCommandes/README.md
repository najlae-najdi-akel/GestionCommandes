# GestionCommandes

Application web Java EE (JEE) de gestion des clients, des produits et des commandes.

## Technologies
- Java 17, Maven (packaging WAR)
- Servlets 4.0 / JSP / JSTL 1.2 (Tomcat 9)
- MySQL 8 avec MySQL Connector/J 8.0.33
- Architecture en couches : Entities, DAO, Service, Servlet, JSP

## Fonctionnalités
- Clients : ajout, liste, modification, suppression
- Produits : ajout, liste, modification, suppression
- Commandes : création avec plusieurs lignes, consultation du détail et du total,
  changement de statut
- Gestion du stock : décrémenté à chaque commande, commande refusée si le stock est
  insuffisant (transaction avec commit / rollback)

## Prérequis
JDK 17, Maven, Apache Tomcat 9, MySQL Server 8, Eclipse IDE for Enterprise Java.

## Installation
1. Exécuter le script `database.sql` dans MySQL Workbench. Il crée la base
   `gestion_commandes`, les 4 tables et l'utilisateur `gestion_app`.
2. Dans `database.sql`, remplacer `VOTRE_MOT_DE_PASSE` par un mot de passe de votre choix,
   puis mettre le même mot de passe dans
   `src/main/java/ma/fstt/dao/DBConnection.java`.
3. Importer le projet dans Eclipse (Import > Existing Maven Projects).
4. Lancer l'application : clic droit sur le projet > Run As > Run on Server (Tomcat 9).
5. Ouvrir http://localhost:8080/GestionCommandes/

## Structure
- `ma.fstt.entities` : Client, Produit, Commande, LigneDeCommande
- `ma.fstt.dao` : DBConnection, ClientDAO, ProduitDAO, CommandeDAO
- `ma.fstt.service` : ClientService, ProduitService, CommandeService
- `ma.fstt.servlet` : ClientServlet, ProduitServlet, CommandeServlet
- `src/main/webapp/WEB-INF/views` : pages JSP

## Auteure
Najlae Najdi Akel