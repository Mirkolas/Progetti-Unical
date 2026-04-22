package it.unical.demacs.informatica.easyhomebackend.persistence.dao;


import it.unical.demacs.informatica.easyhomebackend.model.UserRole;
import it.unical.demacs.informatica.easyhomebackend.model.Utente;
import it.unical.demacs.informatica.easyhomebackend.persistence.dto.UserRoleDto;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {


    Utente findByPrimaryKey(String username);

    void save(Utente utente);
    void delete(Utente utente) throws Exception;
    void update(Utente utente);


    List<UserRoleDto> findAllUsernamesAndRoles();

    void changeUserRole(String username, UserRole newRole);
}