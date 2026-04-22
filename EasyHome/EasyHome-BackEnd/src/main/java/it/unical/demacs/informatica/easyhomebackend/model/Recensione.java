package it.unical.demacs.informatica.easyhomebackend.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@Setter
@ToString
public class Recensione {


        @Setter
        @Getter

        private Integer id;
        private int rating;
        private String descrizione;
        private String acquirente;
        private int idImmobile;


        public Recensione() {
        }

        public Recensione(int rating, String descrizione) {
            this.rating = rating;  // Assegna il file come byte array
            this.descrizione = descrizione;
        }

    }




