package it.unical.demacs.informatica.easyhomebackend.persistence.dao.jdbc;

import it.unical.demacs.informatica.easyhomebackend.model.Contatti;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.ContattiDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContattiDaoJDBC implements ContattiDao {
    private final Connection connection;

    public ContattiDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Contatti contatto) {
        String query = "INSERT INTO contatti (nome, cognome,email, tipo, domanda) VALUES (?, ?, ?, ?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Imposta i parametri della query
            statement.setString(1, contatto.getNome());  // nome
            statement.setString(2, contatto.getCognome());  // cognome
            statement.setString(3, contatto.getEmail());  // email
            statement.setString(4, contatto.getTipo());  // tipo
            statement.setString(5, contatto.getDomanda());  // domanda

            // Esegui la query di inserimento
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante il salvataggio del contatto", e);
        }
    }
}
