-- Script de création de la base GestionCommandes
CREATE DATABASE IF NOT EXISTS gestion_commandes
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gestion_commandes;

CREATE TABLE IF NOT EXISTS clients (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    telephone VARCHAR(20) NULL
);

CREATE TABLE IF NOT EXISTS produits (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    description VARCHAR(255) NULL,
    prix DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS commandes (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    date_commande TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    statut VARCHAR(30) NULL DEFAULT 'En attente',
    client_id INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(id)
);

CREATE TABLE IF NOT EXISTS lignes_commande (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    commande_id INT NOT NULL,
    produit_id INT NOT NULL,
    quantite INT NOT NULL,
    prix_unitaire DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (commande_id) REFERENCES commandes(id),
    FOREIGN KEY (produit_id) REFERENCES produits(id)
);

-- Utilisateur dédié à l'application
CREATE USER IF NOT EXISTS 'gestion_app'@'localhost' IDENTIFIED BY 'VOTRE_MOT_DE_PASSE';
GRANT SELECT, INSERT, UPDATE, DELETE ON gestion_commandes.* TO 'gestion_app'@'localhost';
FLUSH PRIVILEGES;