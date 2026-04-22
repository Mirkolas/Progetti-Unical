package it.unical.demacs.informatica.easyhomebackend.persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkerDTO {
    private double lat;  // Cambiato da latitudine
    private double lng;  // Cambiato da longitudine

    public MarkerDTO(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
