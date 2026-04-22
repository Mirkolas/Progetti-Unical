package it.unical.demacs.informatica.easyhomebackend.persistence.dao.jdbc.proxy;

import it.unical.demacs.informatica.easyhomebackend.model.*;
import it.unical.demacs.informatica.easyhomebackend.persistence.DBManager;
import it.unical.demacs.informatica.easyhomebackend.persistence.dto.MessaggioDto;

import java.util.ArrayList;
import java.util.List;

public class UtenteProxy extends Utente {

    public UtenteProxy(String username, String password, UserRole role, String firstName, String lastName, String birthdate, String province, String city, String country, String email, String phoneNumber, String address, String gender, String cap, String id) {
        super(username, password, role, firstName,lastName,birthdate,province,city,country,email,phoneNumber,address,gender,cap,id);
    }

    @Override
    public List<Immobile> getImmobili() {
        if(this.immobili==null){
            this.immobili = DBManager.getInstance().getImmobileDao().findByUserId(this.getUsername());
        }
        return this.immobili;
    }

    @Override
    public List<MessaggioDto> getMessaggi() {
        if(this.messaggi==null){
            this.messaggi = new ArrayList<>();
            for(Immobile imm: this.getImmobili()){
                List<MessaggioDto> messaggiPerId =DBManager.getInstance().getMessaggioDao().findByImmobileId(imm.getId());
                this.messaggi.addAll(messaggiPerId);
            }
        }
        return this.messaggi;
    }

    @Override
    public List<Recensione> getRecensioni() {
        if(this.recensioni==null){
            this.recensioni = new ArrayList<>();
            for(Immobile imm: this.getImmobili()){
                List<Recensione> recensioniPerId =DBManager.getInstance().getRecensioneDao().findByImmobileId(imm.getId());
                this.recensioni.addAll(recensioniPerId);
            }
        }
        return this.recensioni;
    }
}
