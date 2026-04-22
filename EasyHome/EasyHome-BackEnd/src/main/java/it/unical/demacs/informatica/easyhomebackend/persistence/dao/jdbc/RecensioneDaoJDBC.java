package it.unical.demacs.informatica.easyhomebackend.persistence.dao.jdbc;

import it.unical.demacs.informatica.easyhomebackend.model.Immobile;
import it.unical.demacs.informatica.easyhomebackend.model.Recensione;
import it.unical.demacs.informatica.easyhomebackend.persistence.DBManager;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.ImmobileDao;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.RecensioneDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecensioneDaoJDBC implements RecensioneDao {
    private final Connection connection;

    public RecensioneDaoJDBC(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void save(Recensione recensione) {
        String query = "INSERT INTO recensione (id, rating, descrizione, acquirente, idImmobile) " +
                "VALUES (COALESCE(?, nextval('recensione_id_seq')), ?, ?, ?, ?) ";
        try (PreparedStatement s = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            if (recensione.getId() != null) {
                s.setObject(1, recensione.getId());
            } else {
                s.setNull(1, Types.INTEGER);
            }

            s.setInt(2, recensione.getRating());
            s.setString(3, recensione.getDescrizione());
            s.setString(4, recensione.getAcquirente());
            s.setInt(5, recensione.getIdImmobile());

            s.executeUpdate();

            try (ResultSet rs = s.getGeneratedKeys()) {
                if (rs.next()) {
                    recensione.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Inserimento messaggio fallito, nessun ID generato.");
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante il salvataggio del messaggio", e);
        }
    }

    @Override
    public void deleteByImmobileId(int idImmobile) {
        String sql = "DELETE FROM recensione WHERE idimmobile = ?";  // Query per eliminare recensioni di un immobile

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idImmobile);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante l'eliminazione delle recensioni dell'immobile con ID: " + idImmobile, e);
        }
    }


    @Override
    public List<Recensione> findByImmobileId(int idImmobile) {
        String sql = "SELECT * FROM recensione WHERE idimmobile = ?";  // Query per ottenere username e ruolo
        List<Recensione> recensioni = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idImmobile);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Recensione recensione = new Recensione(resultSet.getInt("rating"),resultSet.getString("descrizione"));
                recensione.setId(resultSet.getInt("id"));
                recensione.setAcquirente(resultSet.getString("acquirente"));
                ImmobileDao immobileDao = DBManager.getInstance().getImmobileDao();
                Immobile immobile = immobileDao.findByPrimaryKey(resultSet.getInt("idimmobile"));
                recensione.setIdImmobile(immobile.getId());
                recensioni.add(recensione);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recensioni;
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM recensione WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Nessuna recensione trovata con id: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
