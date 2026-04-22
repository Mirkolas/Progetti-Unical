package it.unical.demacs.informatica.easyhomebackend.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@Setter
public class ImmobileDto {
    private String nome;
    private String tipo;
    private String descrizione;
    private String categoria;
    private int prezzo;
    private int mq;
    private int camere;
    private int bagni;
    private int anno;
    private String data;
    private String provincia;
    private double latitudine;
    private double longitudine;
    private List<MultipartFile> foto;
    private String user;

}
