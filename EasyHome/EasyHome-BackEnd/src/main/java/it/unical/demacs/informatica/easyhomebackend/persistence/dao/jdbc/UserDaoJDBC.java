package it.unical.demacs.informatica.easyhomebackend.persistence.dao.jdbc;



import it.unical.demacs.informatica.easyhomebackend.model.Immobile;
import it.unical.demacs.informatica.easyhomebackend.model.Recensione;
import it.unical.demacs.informatica.easyhomebackend.model.UserRole;
import it.unical.demacs.informatica.easyhomebackend.model.Utente;
import it.unical.demacs.informatica.easyhomebackend.persistence.DBManager;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.ImmobileDao;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.MessaggioDao;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.RecensioneDao;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.UserDao;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.jdbc.proxy.UtenteProxy;
import it.unical.demacs.informatica.easyhomebackend.persistence.dto.MessaggioDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import it.unical.demacs.informatica.easyhomebackend.persistence.dto.UserRoleDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBC  implements UserDao {

    Connection connection;


    public UserDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Utente findByPrimaryKey(String username) {
        String sql = "SELECT * FROM utente WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UtenteProxy(
                        rs.getString("username"),
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role")),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("data_nascita"),
                        rs.getString("provincia"),
                        rs.getString("citta"),
                        rs.getString("nazionalita"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("indirizzo"),
                        rs.getString("sesso"),
                        rs.getString("cap"),
                        rs.getString("cf")
                );
            }
            throw new UsernameNotFoundException("Utente non trovato");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Utente utente) {
        String query = "INSERT INTO utente (username, password , role,nome,cognome, data_nascita, nazionalita, email, provincia,citta,telefono,indirizzo,sesso,cap,cf) VALUES (?, ? , ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?) " ;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, utente.getUsername());
            statement.setString(2, utente.getPassword());
            statement.setString(3, utente.getRole().toString());
            statement.setString(4, utente.getFirstName());
            statement.setString(5, utente.getLastName());
            statement.setString(6, utente.getBirthdate());
            statement.setString(7, utente.getCountry());
            statement.setString(8, utente.getEmail());
            statement.setString(9, utente.getProvince());
            statement.setString(10, utente.getCity());
            statement.setString(11, utente.getPhoneNumber());
            statement.setString(12, utente.getAddress());
            statement.setString(13, utente.getGender());
            statement.setString(14, utente.getCap());
            statement.setString(15, utente.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Utente utente) {


    }
    @Override
    public void delete(Utente utente) throws Exception {
        String query = "DELETE FROM utente WHERE username = ?";
        List<Immobile> immobili = utente.getImmobili();
        List<MessaggioDto> messaggi = utente.getMessaggi();
        List<Recensione> recensioni =utente.getRecensioni();
        ImmobileDao immobileDao = DBManager.getInstance().getImmobileDao();
        MessaggioDao messaggioDao = DBManager.getInstance().getMessaggioDao();
        RecensioneDao recensioneDao = DBManager.getInstance().getRecensioneDao();
        for(Immobile imm: immobili) immobileDao.deleteimmobileID(imm.getId());
        for(MessaggioDto mes: messaggi) messaggioDao.delete(mes.getId());
        for(Recensione rec: recensioni) recensioneDao.delete(rec.getId());

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, utente.getUsername());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Nessun utente trovato con username: " + utente.getUsername());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public List<UserRoleDto> findAllUsernamesAndRoles() {
        String sql = "SELECT username, role FROM utente";  // Query per ottenere username e ruolo
        List<UserRoleDto> userList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            // Elenco di username e ruolo dalla risposta del database
            while (resultSet.next()) {
                String user = resultSet.getString("username");
                String role = resultSet.getString("role");
                userList.add(new UserRoleDto(user, UserRole.valueOf(role)));  // Crea oggetti UserRoleDto
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    @Override
    public void changeUserRole(String username, UserRole newRole) {
        String sql = "UPDATE utente SET role = ? WHERE username = ?"; // Query per aggiornare il ruolo

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newRole.name()); // Converte l'enum UserRole in stringa
            statement.setString(2, username);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}