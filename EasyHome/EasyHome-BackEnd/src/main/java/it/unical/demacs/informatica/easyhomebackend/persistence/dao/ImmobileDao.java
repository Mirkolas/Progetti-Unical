package it.unical.demacs.informatica.easyhomebackend.persistence.dao;

import it.unical.demacs.informatica.easyhomebackend.model.Immobile;
import it.unical.demacs.informatica.easyhomebackend.model.ImmobileMinimal;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ImmobileDao {

        void save(Immobile immobile, String user);

        List<byte[]> getImmagini(Integer id) throws Exception;

        Immobile findByPrimaryKey(int id);

        List<Immobile> findByUserId(String username);

        List<Immobile> findAll();

        List<ImmobileMinimal> getImmobiliFilteredMinimal(String tipo, String categoria, String provincia);

        Optional<Immobile> findById(int id);

        void deleteimmobileID(int id) throws Exception;

        void update(Immobile immobile);

        List<ImmobileMinimal> getImmobiliMinimalByUsername(String username);

}
