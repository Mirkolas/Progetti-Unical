package it.unical.ea.eventra.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BigliettoCreateDTO {
    @NotBlank(message = "Il nome dello spettatore è campo obbligatorio!")
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ]+(?: [A-Za-zÀ-ÖØ-öø-ÿ]+)*$", message = "Il nome deve contenere solo lettere e spazi.")
    private String nomeSpettatore;

    @NotBlank(message = "Il cognome dello spettatore è campo obbligatorio!")
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ]+(?: [A-Za-zÀ-ÖØ-öø-ÿ]+)*$", message = "Il cognome deve contenere solo lettere e spazi.")
    private String cognomeSpettatore;

    @Email(message = "Email non valida")
    @NotBlank(message = "Email è obbligatoria")
    private String emailSpettatore;

    @NotNull(message = "ID evento è obbligatorio")
    private Long eventoId;

    @NotNull(message = "ID tipo posto è obbligatorio")
    private Long tipoPostoId;

    @NotNull(message = "ID pagamento è obbligatorio")
    private Long pagamentoId;
}
