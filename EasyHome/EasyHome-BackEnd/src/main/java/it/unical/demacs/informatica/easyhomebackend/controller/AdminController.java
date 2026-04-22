
package it.unical.demacs.informatica.easyhomebackend.controller;



import it.unical.demacs.informatica.easyhomebackend.model.UserRole;
import it.unical.demacs.informatica.easyhomebackend.model.Utente;
import it.unical.demacs.informatica.easyhomebackend.persistence.dto.UserRoleDto;
import it.unical.demacs.informatica.easyhomebackend.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final IUserService userservice;

    public AdminController(IUserService userservice) {
        this.userservice = userservice;
    }


    @PostMapping("/change_role")
    public ResponseEntity<?> changeUserRole(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String newRole = request.get("newRole");
        System.out.println(username + " " + newRole);

        if (username == null || newRole == null) {
            return ResponseEntity.badRequest().body("Errore: username e newRole sono obbligatori.");
        }

        userservice.changeUserRole(username, UserRole.valueOf(newRole));
        return ResponseEntity.ok("Ruolo aggiornato con successo!");
    }



    @DeleteMapping("/users/{username}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String username) {
        boolean isDeleted = userservice.deleteUser(username);
        Map<String, String> response = new HashMap<>();
        if (isDeleted) {
            response.put("message", "Utente eliminato con successo");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Utente non trovato");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}