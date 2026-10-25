package ma.fstt.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {

    public static void main(String[] args) {
        try (Connection cn = DBConnection.getConnection()) {
            System.out.println("Connexion réussie !");
            System.out.println("Base de données : " + cn.getCatalog());
            System.out.println("Version MySQL   : " + cn.getMetaData().getDatabaseProductVersion());
        } catch (SQLException e) {
            System.out.println("Échec de la connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }
}