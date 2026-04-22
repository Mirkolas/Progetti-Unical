package it.unical.ea.eventra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAuthentication {
    private String token;
    private String refreshToken;
    private Long utente;
    private String role;

    public ResponseAuthentication(String token, String refreshToken, Long utente) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.utente = utente;
        this.role = "USER"; 
    }
}
