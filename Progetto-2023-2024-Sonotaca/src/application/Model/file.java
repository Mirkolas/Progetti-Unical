package application.Model;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class file {
    public List<String> leggi(String nomeFile) throws IOException {
        return Files.readAllLines(Path.of(nomeFile));
    }
}
