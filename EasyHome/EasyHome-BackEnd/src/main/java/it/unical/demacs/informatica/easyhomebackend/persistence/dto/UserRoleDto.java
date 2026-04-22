package it.unical.demacs.informatica.easyhomebackend.persistence.dto;

import it.unical.demacs.informatica.easyhomebackend.model.UserRole;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRoleDto {
    private String username;
    private UserRole role;

    public UserRoleDto(String username, UserRole role) {
        this.username = username;
        this.role = role;
    }

    public UserRole getNewRole() {
        return this.role;
    }
}
