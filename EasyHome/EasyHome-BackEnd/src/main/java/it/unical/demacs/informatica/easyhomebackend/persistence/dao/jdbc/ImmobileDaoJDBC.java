package it.unical.demacs.informatica.easyhomebackend.persistence.dao.jdbc;

import it.unical.demacs.informatica.easyhomebackend.model.Immobile;
import it.unical.demacs.informatica.easyhomebackend.model.ImmobileMinimal;
import it.unical.demacs.informatica.easyhomebackend.persistence.DBManager;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.ImmobileDao;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.RecensioneDao;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;


public class ImmobileDaoJDBC implements ImmobileDao {
    private final Connection connection;
    public ImmobileDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    public static final String immobiliImagesDir = String.valueOf(Path.of("immobiliImages"));
    @Override
    public Immobile findByPrimaryKey(int id) {
        String query = "SELECT * FROM immobile WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return createImm(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante il recupero dell'immobile", e);
        }
        return null;
    }

    private Immobile createImm(ResultSet resultSet) throws SQLException {

        Array array = resultSet.getArray("immagini");
        List<String> immagini = new ArrayList<>();
        if (array != null) {
            Collections.addAll(immagini, (String[]) array.getArray());
        }

        // Creazione dell'oggetto Immobile con foto convertite
        return new Immobile(
                resultSet.getInt("id"),
                resultSet.getString("nome"),
                resultSet.getString("tipo"),
                resultSet.getString("descrizione"),
                resultSet.getString("categoria"),
                resultSet.getInt("prezzo"),
                resultSet.getInt("mq"),
                resultSet.getInt("camere"),
                resultSet.getInt("bagni"),
                resultSet.getInt("anno"),
                resultSet.getString("data"),
                resultSet.getString("provincia"),
                resultSet.getDouble("latitudine"),
                resultSet.getDouble("longitudine"),
                immagini,
                resultSet.getInt("prezzo_scontato")
        );
    }


    @Override
    public List<Immobile> findAll() {
        List<Immobile> immobili = new ArrayList<>();
        String query = "SELECT * FROM immobile";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Immobile immobile = createImm(resultSet);
                immobili.add(immobile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante il recupero degli immobili", e);
        }
        return immobili;
    }

    @Override
    public List<byte[]> getImmagini(Integer id) throws Exception {
        Immobile immobile = findByPrimaryKey(id);
        List<String> immobiliFotoPath = immobile.getFotoPaths();
        List<byte[]> immobiliFoto = new ArrayList<>();
        for (String path : immobiliFotoPath) {
            Path p = Path.of(path);
            immobiliFoto.add(Files.readAllBytes(p));
        }
        return immobiliFoto;
    }

    @Override
    public void save(Immobile immobile, String user) {
        String queryImmobile = "INSERT INTO immobile (id, nome, tipo, descrizione, categoria, prezzo, mq, camere, bagni, anno, data, provincia, latitudine, longitudine, immagini, venditore) " +
                "VALUES (COALESCE(?, nextval('immobili_id_seq')),?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET " +
                "nome=EXCLUDED.nome, " +
                "tipo=EXCLUDED.tipo, " +
                "descrizione=EXCLUDED.descrizione, " +
                "categoria=EXCLUDED.categoria, " +
                "prezzo=EXCLUDED.prezzo, " +
                "mq=EXCLUDED.mq, " +
                "camere=EXCLUDED.camere, " +
                "bagni=EXCLUDED.bagni, " +
                "anno=EXCLUDED.anno, " +
                "data=EXCLUDED.data, " +
                "provincia=EXCLUDED.provincia, " +
                "latitudine=EXCLUDED.latitudine, " +
                "longitudine=EXCLUDED.longitudine, " +
                "immagini=EXCLUDED.immagini, " +
                "venditore=EXCLUDED.venditore, " +
                "prezzo_scontato=EXCLUDED.prezzo_scontato " +
                "RETURNING id";
        try (PreparedStatement statementImmobile = connection.prepareStatement(queryImmobile, Statement.RETURN_GENERATED_KEYS)) {
            if (immobile.getId() != null) {
                statementImmobile.setObject(1, immobile.getId());
            } else {
                statementImmobile.setNull(1, Types.INTEGER);
            }
            statementImmobile.setString(2, immobile.getNome());
            statementImmobile.setString(3, immobile.getTipo());
            statementImmobile.setString(4, immobile.getDescrizione());
            statementImmobile.setString(5, immobile.getCategoria());
            statementImmobile.setDouble(6, immobile.getPrezzo());
            statementImmobile.setInt(7, immobile.getMq());
            statementImmobile.setInt(8, immobile.getCamere());
            statementImmobile.setInt(9, immobile.getBagni());
            statementImmobile.setInt(10, immobile.getAnno());
            statementImmobile.setString(11, immobile.getData());
            statementImmobile.setString(12, immobile.getProvincia());
            statementImmobile.setDouble(13, immobile.getLatitudine());
            statementImmobile.setDouble(14, immobile.getLongitudine());
            List<String> imagesPaths = immobile.getFotoPaths();
            Array sqlArray = connection.createArrayOf("text", imagesPaths.toArray());
            statementImmobile.setArray(15, sqlArray);
            statementImmobile.setString(16, user);
            // Esegui l'update dell'immobile e ottieni l'ID generato
            int affectedRows = statementImmobile.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserimento immobile fallito, nessuna riga modificata.");
            }

            // Recupera l'ID dell'immobile appena inserito
            try (ResultSet rs = statementImmobile.getGeneratedKeys()) {
                if (rs.next()) {
                    immobile.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Inserimento immobile fallito, nessun ID generato.");
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante il salvataggio dell'immobile", e);
        }
    }

    @Override
    public List<Immobile> findByUserId(String username) {
        String sql = "SELECT * FROM immobile WHERE venditore = ?";
        List<Immobile> immobili = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Immobile immobile = createImm(rs);
                immobile.setFoto(getImmagini(rs.getInt("id")));
                immobili.add(immobile);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return immobili;
    }



    // VA SISTEMATO TUTTO
    @Override
    public List<ImmobileMinimal> getImmobiliFilteredMinimal(String tipo, String categoria, String provincia) {
        List<ImmobileMinimal> immobiliMinimal = new ArrayList<>();

        String query = "SELECT id, nome, prezzo, tipo, categoria, mq, immagini, prezzo_scontato FROM immobile";
        List<String> conditions = new ArrayList<>();

        if (!Objects.equals(tipo, "Tutti")) {
            conditions.add("tipo = ?");
        }

        // Se la categoria è "Tutti", escludi gli immobili con categoria "Aste"
        if (Objects.equals(categoria, "Tutti")) {
            conditions.add("categoria != 'Aste'");
        } else {
            conditions.add("categoria = ?");
        }

        if (!Objects.equals(provincia, "Tutte")) {
            conditions.add("provincia = ?");
        }

        if (!conditions.isEmpty()) {
            query += " WHERE " + String.join(" AND ", conditions);
        }



        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            int paramIndex = 1;

            if (!Objects.equals(tipo, "Tutti")) {
                pstmt.setString(paramIndex++, tipo);
            }
            if (!Objects.equals(categoria, "Tutti")) {
                pstmt.setString(paramIndex++, categoria);
            }
            if (!Objects.equals(provincia, "Tutte")) {
                pstmt.setString(paramIndex, provincia);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Integer id  = rs.getInt("id");
                    String nome = rs.getString("nome");
                    int prezzo = rs.getInt("prezzo");
                    String tipoImmobile = rs.getString("tipo");
                    String categoriaImmobile = rs.getString("categoria");
                    int prezzo_scontato = rs.getInt("prezzo_scontato");
                    int mq = rs.getInt("mq");
                    // Recupero della prima immagine dell'array
                    Array immaginiArray = rs.getArray("immagini");
                    String primaImmagine = null;

                    if (immaginiArray != null) {
                        String[] immagini = (String[]) immaginiArray.getArray();
                        if (immagini.length > 0) {
                            primaImmagine = immagini[0]; // Prende solo la prima immagine
                        }
                    }

                    immobiliMinimal.add(new ImmobileMinimal(id, nome, prezzo, tipoImmobile, categoriaImmobile, mq, primaImmagine,prezzo_scontato));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'esecuzione della query", e);
        }


        return immobiliMinimal;
    }

    private void deleteImmobileImages(int immobileId) throws Exception {
        File immobileDirectory = new File(immobiliImagesDir +"\\" + immobileId);

        if (immobileDirectory.exists() && immobileDirectory.isDirectory()) {
            File[] files = immobileDirectory.listFiles();

            if (files != null) {

                for (File file : files) {
                    if (file.exists() && !file.delete()) {
                        throw new Exception("Impossibile eliminare il file: " + file.getAbsolutePath());
                    }
                }
            }

            // Dopo aver eliminato i file, ora possiamo eliminare la cartella
            if (!immobileDirectory.delete()) {
                throw new Exception("Impossibile eliminare la cartella: " + immobileDirectory.getAbsolutePath());
            }
        } else {
            System.out.println("Cartella inesistente: " + immobileDirectory.getAbsolutePath());
        }
    }



    @Override
    public void deleteimmobileID(int id) throws Exception {
        deleteImmobileImages(id);
        RecensioneDao recensioneDao = DBManager.getInstance().getRecensioneDao();
        recensioneDao.deleteByImmobileId(id);
        String query = "DELETE FROM immobile WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Immobile con ID " + id + " non trovato.");
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'eliminazione dell'immobile con ID: " + id, e);
        }
    }

    @Override
    public void update(Immobile immobile) {

        if (immobile.getId() == null) {
            throw new IllegalArgumentException("ID immobile mancante per l'aggiornamento");
        }

        String querySelect = "SELECT prezzo FROM immobile WHERE id = ?";
        double prezzoAttuale;

        // Recupera il prezzo attuale e il prezzo_scontato dal database
        try (PreparedStatement statementSelect = connection.prepareStatement(querySelect)) {
            statementSelect.setInt(1, immobile.getId());
            try (ResultSet resultSet = statementSelect.executeQuery()) {
                if (resultSet.next()) {
                    prezzoAttuale = resultSet.getDouble("prezzo");
                } else {
                    throw new SQLException("Immobile non trovato con ID " + immobile.getId());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero del prezzo dell'immobile", e);
        }
        Integer prezzoScontato = null;

        // Se il prezzo passato è diverso, aggiorna prezzo_scontato, altrimenti lo lascia invariato
        if(prezzoAttuale!=immobile.getPrezzo()) {
            prezzoScontato = immobile.getPrezzo();
        }

        String queryUpdate =
                "UPDATE immobile SET " +
                        "nome = ?, " +
                        "tipo = ?, " +
                        "descrizione = ?, " +
                        "categoria = ?, " +
                        "mq = ?, " +
                        "camere = ?, " +
                        "bagni = ?, " +
                        "anno = ?, " +
                        "data = ?, " +
                        "provincia = ?, " +
                        "latitudine = ?, " +
                        "longitudine = ?, " +
                        "immagini = ?, " +
                        "prezzo_scontato = ? " + // Aggiorna solo se necessario
                        "WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(queryUpdate)) {
            // Imposta i parametri per l'UPDATE
            statement.setString(1, immobile.getNome());
            statement.setString(2, immobile.getTipo());
            statement.setString(3, immobile.getDescrizione());
            statement.setString(4, immobile.getCategoria());
            statement.setInt(5, immobile.getMq());
            statement.setInt(6, immobile.getCamere());
            statement.setInt(7, immobile.getBagni());
            statement.setInt(8, immobile.getAnno());
            statement.setString(9, immobile.getData());
            statement.setString(10, immobile.getProvincia());
            statement.setDouble(11, immobile.getLatitudine());
            statement.setDouble(12, immobile.getLongitudine());

            // Conversione lista immagini in SQL Array
            Array sqlArray = connection.createArrayOf("text", immobile.getFotoPaths().toArray());
            statement.setArray(13, sqlArray);

            if(prezzoScontato==null) statement.setNull(14,Types.INTEGER);
            else statement.setInt(14, prezzoScontato); // Mantiene o aggiorna prezzo_scontato
            statement.setInt(15, immobile.getId()); // ID per WHERE

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Aggiornamento fallito: nessun immobile trovato con ID " + immobile.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'aggiornamento dell'immobile", e);
        }
    }



    @Override
    public List<ImmobileMinimal> getImmobiliMinimalByUsername(String username) {
        List<ImmobileMinimal> immobiliMinimal = new ArrayList<>();

        List<Immobile> immobili = DBManager.getInstance().getUserDao().findByPrimaryKey(username).getImmobili();
        for(Immobile imm: immobili){
            immobiliMinimal.add(new ImmobileMinimal(imm.getId(), imm.getNome(), imm.getPrezzo(), imm.getTipo(), imm.getCategoria(), imm.getMq(), imm.getFotoPaths().getFirst(),imm.getPrezzo_scontato()));
        }

        return immobiliMinimal;
    }




    @Override
    public Optional<Immobile> findById(int id) {
        String query = "SELECT * FROM immobile WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String venditore = rs.getString("venditore");
                Immobile immobile = createImm(rs);
                immobile.setUtente(DBManager.getInstance().getUserDao().findByPrimaryKey(venditore));
                return Optional.of(immobile);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
