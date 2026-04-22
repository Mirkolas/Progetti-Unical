package it.unical.ea.eventra.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import it.unical.ea.eventra.conf.ApiResponseConfiguration;
import it.unical.ea.eventra.data.entity.Utente;
import it.unical.ea.eventra.data.service.UtenteService;
import it.unical.ea.eventra.data.service.WishlistService;
import it.unical.ea.eventra.dto.ResponseAuthentication;
import it.unical.ea.eventra.dto.WishlistDTO;
import it.unical.ea.eventra.core.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "OAuth", description = "Gestione login OAuth e recupero informazioni utente")
public class OAuthController {

    private final UtenteService utenteService;
    private final JwtService jwtService;
    private final WishlistService wishlistService;

    @Operation(
            summary = "Login con OAuth2",
            description = "Endpoint chiamato automaticamente dopo un login OAuth2 riuscito. Recupera le informazioni dell'utente da Google (o altro provider), crea un nuovo utente nel sistema se non esiste già, genera JWT e refresh token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utente autenticato con successo"),
            @ApiResponse(responseCode = "401", description = "Autenticazione fallita o token non valido"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping("/oauth/success")
    public ResponseEntity<ApiResponseConfiguration<ResponseAuthentication>> getLoginInfo(OAuth2AuthenticationToken authentication) {
        try {
            Map<String, Object> attributes = authentication.getPrincipal().getAttributes();
            String email = (String) attributes.get("email");

            // Ottieni o crea l'utente
            Utente utente = utenteService.getOrCreateUser(email, attributes);

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    utente.getEmail(),
                    utente.getPassword(),
                    utente.getAuthorities()
            );

            // Genera JWT e refresh token
            String jwt = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            // Crea wishlist se non esiste già
            try {
                WishlistDTO wishlistDTO = new WishlistDTO();
                wishlistDTO.setUtenteId(utente.getId());
                wishlistService.create(wishlistDTO);
            } catch (Exception e) {
                // Ignora se la wishlist esiste già
            }

            ResponseAuthentication responseAuthentication = new ResponseAuthentication(
                    jwt,
                    refreshToken,
                    utente.getId(),
                    utente.getRole().name()
            );

            return ResponseEntity.ok(
                    new ApiResponseConfiguration<>(
                            true,
                            "Login OAuth2 effettuato con successo!",
                            responseAuthentication
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponseConfiguration<>(
                            false,
                            "Errore durante il login OAuth2: " + e.getMessage(),
                            null
                    )
            );
        }
    }
}
