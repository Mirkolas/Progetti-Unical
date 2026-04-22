package it.unical.demacs.informatica.easyhomebackend.persistence.dao.jdbc;

import it.unical.demacs.informatica.easyhomebackend.model.Immobile;
import it.unical.demacs.informatica.easyhomebackend.model.Messaggio;
import it.unical.demacs.informatica.easyhomebackend.model.Utente;
import it.unical.demacs.informatica.easyhomebackend.persistence.DBManager;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.ImmobileDao;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.MessaggioDao;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.UserDao;
import it.unical.demacs.informatica.easyhomebackend.persistence.dto.MessaggioDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessaggioDaoJDBC implements MessaggioDao {
    private final Connection connection;

    public MessaggioDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Messaggio messaggio, String acquirente, int immobileId) {
        String query = "INSERT INTO messaggio (id, oggetto, descrizione, acquirente, email, telefono, idImmobile) " +
                "VALUES (COALESCE(?, nextval('messaggio_id_seq')), ?, ?, ?, ?, ?, ?) ";
        try (PreparedStatement s = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            if (messaggio.getId() != null) {
                s.setObject(1, messaggio.getId());
            } else {
                s.setNull(1, Types.INTEGER);
            }

            s.setString(2, messaggio.getOggetto());
            s.setString(3, messaggio.getDescrizione());
            s.setString(4, acquirente);
            UserDao utenteDao = DBManager.getInstance().getUserDao();
            Utente utente = utenteDao.findByPrimaryKey(acquirente);
            s.setString(5, utente.getEmail());
            s.setLong(6, Long.parseLong(utente.getPhoneNumber()));
            s.setInt(7, immobileId);

            s.executeUpdate();

            try (ResultSet rs = s.getGeneratedKeys()) {
                if (rs.next()) {
                    messaggio.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Inserimento messaggio fallito, nessun ID generato.");
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante il salvataggio del messaggio", e);
        }
    }

    public List<MessaggioDto> findByImmobileId(int idImmobile) {
        String sql = "SELECT * FROM messaggio WHERE idimmobile = ?";  // Query per ottenere username e ruolo
        List<MessaggioDto> messaggi = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idImmobile);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                MessaggioDto messaggio = new MessaggioDto();
                messaggio.setId(resultSet.getInt("id"));
                messaggio.setOggetto(resultSet.getString("oggetto"));
                messaggio.setDescrizione(resultSet.getString("descrizione"));
                messaggio.setAcquirente(resultSet.getString("acquirente"));
                messaggio.setEmail(resultSet.getString("email"));
                messaggio.setTelefono(resultSet.getLong("telefono"));
                ImmobileDao immobileDao = DBManager.getInstance().getImmobileDao();
                Immobile immobile = immobileDao.findByPrimaryKey(resultSet.getInt("idimmobile"));
                messaggio.setIdImmobile(immobile.getId());
                messaggio.setNomeImmobile(immobile.getNome());
                messaggi.add(messaggio);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return messaggi;
    }

    @Override
    public void delete(int idMessaggio) {
        String query = "DELETE FROM messaggio WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idMessaggio);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Nessun messaggio trovato con ID: " + idMessaggio);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteAllByImmobileId(int idImmobile) {
        String query = "DELETE FROM messaggio WHERE idImmobile = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idImmobile);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Nessun messaggio trovato con idimmobile: " + idImmobile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
