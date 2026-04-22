package it.unical.demacs.informatica.easyhomebackend.persistence;

import it.unical.demacs.informatica.easyhomebackend.persistence.dao.*;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.jdbc.*;
import lombok.Getter;

import java.sql.*;

public class DBManager {

    private UserDao userDao = null;
    private ImmobileDao immobileDao = null;
    private RecensioneDao recensioneDao = null;
    private ContattiDao contattiDao = null;
    private MessaggioDao messaggioDao = null;
    private static DBManager instance;
    @Getter
    private Connection connection;

    // Variabili separate per username e password
    private static final String DB_USER = "POSTGRES_USER";  // Username del database
    private static final String DB_PASSWORD = "POSTGRES_PASSWORD";  // Password del database

    private DBManager() {
        try {
            Class.forName("org.postgresql.Driver");

            // Crea la connessione utilizzando i dettagli del database
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/EasyHome", // URL del database
                    DB_USER, // Nome utente da variabile separata
                    DB_PASSWORD // Password da variabile separata
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante la connessione al database", e);
        }
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDaoJDBC(getConnection());
        }
        return userDao;
    }

    public MessaggioDao getMessaggioDao() {
        if (messaggioDao == null) {
            messaggioDao = new MessaggioDaoJDBC(getConnection());
        }
        return messaggioDao;
    }

    public ImmobileDao getImmobileDao() {
        if (immobileDao == null) {
            immobileDao = new ImmobileDaoJDBC(getConnection());
        }
        return immobileDao;
    }

    public RecensioneDao getRecensioneDao() {
        if (recensioneDao == null) {
            recensioneDao = new RecensioneDaoJDBC(getConnection());
        }
        return recensioneDao;
    }

    public ContattiDao getContattiDao() {
        if (contattiDao == null) {
            contattiDao = new ContattiDaoJDBC(getConnection());
        }
        return contattiDao;
    }
}
