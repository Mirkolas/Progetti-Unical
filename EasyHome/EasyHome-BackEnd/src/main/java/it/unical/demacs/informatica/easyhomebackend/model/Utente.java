package it.unical.demacs.informatica.easyhomebackend.model;

import it.unical.demacs.informatica.easyhomebackend.persistence.dto.MessaggioDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@ToString
public class Utente implements UserDetails {


    private String username;
    private String password;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String birthdate;
    private String province;
    private String city;
    private String country;
    private String email;
    private String phoneNumber;
    private String address;
    private String gender;
    private String cap;
    private String id;
    protected List<Immobile> immobili;
    protected List<MessaggioDto> messaggi;
    protected List<Recensione> recensioni;

    public Utente(String username, String password, UserRole role, String firstName, String lastName, String birthdate, String province, String city, String country, String email, String phoneNumber, String address, String gender, String cap, String id) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.province = province;
        this.city = city;
        this.country = country;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
        this.cap = cap;
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return role.toString();
                    }
                }
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}