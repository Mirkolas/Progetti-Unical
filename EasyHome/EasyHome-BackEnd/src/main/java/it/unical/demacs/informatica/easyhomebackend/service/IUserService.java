package it.unical.demacs.informatica.easyhomebackend.service;


import it.unical.demacs.informatica.easyhomebackend.model.UserRole;
import it.unical.demacs.informatica.easyhomebackend.model.Utente;
import it.unical.demacs.informatica.easyhomebackend.persistence.dto.UserRoleDto;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    void createUser(Utente utente);

    public boolean deleteUser(String username);
    Optional<Utente> getUser(String username);


    List<UserRoleDto> getAllUsernamesAndRoles();

    void changeUserRole(String username, UserRole newRole);
}