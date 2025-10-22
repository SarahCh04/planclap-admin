package infrastructures.json;

import org.helmo.planclap_admin.domains.Movie;
import org.helmo.planclap_admin.infrastructures.JsonMovieRepository;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonMovieRepositoryTest {

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        // Crée un répertoire temporaire isolé pour chaque test
        tempDir = Files.createTempDirectory("planclap_test_");
    }

    @AfterEach
    void tearDown() throws IOException {
        // Supprime récursivement le dossier temporaire
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    @DisplayName("Si le fichier JSON n'existe pas, il est créé vide et la liste renvoyée est vide")
    void testCreateEmptyFileIfNotExists() {
        File dir = tempDir.toFile();
        File jsonFile = new File(dir, "movies.json");
        assertFalse(jsonFile.exists());

        JsonMovieRepository repo = new JsonMovieRepository(dir);
        List<Movie> movies = repo.loadMovies();

        assertTrue(jsonFile.exists(), "Le fichier movies.json devrait avoir été créé");
        assertEquals(0, movies.size(), "La liste des films devrait être vide");
    }

    @Test
    @DisplayName("Si le fichier existe avec un contenu JSON valide, il retourne les bons films")
    void testLoadMoviesFromExistingFile() throws IOException {
        File jsonFile = new File(tempDir.toFile(), "movies.json");

        String json = """
                {
                  "movies": [
                    {"title": "Vaiana 2", "duration": 100, "seances": 5},
                    {"title": "Dune : Deuxième Partie", "duration": 166, "sceances": 3}
                  ]
                }
                """;

        Files.writeString(jsonFile.toPath(), json);

        JsonMovieRepository repo = new JsonMovieRepository(tempDir.toFile());
        List<Movie> movies = repo.loadMovies();

        assertEquals(2, movies.size());
        assertEquals("Vaiana 2", movies.get(0).getTitle());
        assertEquals(100, movies.get(0).getDuration());
        assertEquals(5, movies.get(0).getSeances());
    }

    @Test
    @DisplayName("Si le fichier est mal formé, une liste vide est renvoyée sans planter l'appli")
    void testMalformedJsonReturnsEmptyList() throws IOException {
        File jsonFile = new File(tempDir.toFile(), "movies.json");
        Files.writeString(jsonFile.toPath(), "{ invalid json !!!");

        JsonMovieRepository repo = new JsonMovieRepository(tempDir.toFile());

        assertDoesNotThrow(() -> {
            List<Movie> movies = repo.loadMovies();
            assertTrue(movies.isEmpty(), "Un JSON invalide doit renvoyer une liste vide");
        });
    }

    @Test
    @DisplayName("Si le répertoire passé est incorrect, une exception doit être levée")
    void testInvalidDirectoryThrowsException() {
        File invalidDir = new File("Z:/chemin/inexistant/");

        assertThrows(RuntimeException.class, () -> {
            new JsonMovieRepository(invalidDir).loadMovies();
        });
    }
}
