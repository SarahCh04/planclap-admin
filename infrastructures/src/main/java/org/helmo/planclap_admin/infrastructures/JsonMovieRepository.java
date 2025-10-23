package org.helmo.planclap_admin.infrastructures;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.*;
import org.helmo.planclap_admin.domains.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Implémentation du repository de films utilisant un fichier JSON pour le stockage.
 * Cette classe gère la sérialisation/désérialisation des films et implémente
 * la recherche avec tolérance (distance de Levenshtein).
 */
public class JsonMovieRepository implements MovieRepository {

    private static final Logger logger = LogManager.getLogger(JsonMovieRepository.class);

    private final File jsonFile;
    private final Gson gson;

    /**
     * Construit un repository JSON pour les films.
     *
     * @param dir le répertoire contenant le fichier movies.json
     */
    public JsonMovieRepository(File dir) {
        this.jsonFile = new File(dir, "movies.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public List<Movie> loadMovies() {
        if (!jsonFile.exists()) {
            logger.info("Fichier JSON inexistant. Création d'un fichier vide.");
            createEmptyJsonFile();
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(jsonFile)) {
            List<Movie> movies = readMoviesFromJson(reader);
            logger.info("{} films chargés depuis {}", movies.size(), jsonFile.getName());
            return movies;
        } catch (IOException e) {
            logger.error("Erreur lors de la lecture du fichier JSON", e);
            throw new RuntimeException("Erreur lors de la lecture du fichier JSON", e);
        }
    }

    @Override
    public void saveMovies(List<Movie> movies) {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            // Sérialisation directe de la liste de Movie
            JsonObject root = new JsonObject();
            root.add("movies", gson.toJsonTree(movies));

            gson.toJson(root, writer);
            logger.info("{} films sauvegardés dans {}", movies.size(), jsonFile.getName());

        } catch (IOException e) {
            logger.error("Erreur lors de l'écriture du fichier JSON", e);
            throw new RuntimeException("Erreur lors de l'écriture du fichier JSON", e);
        }
    }

    @Override
    public Optional<Movie> findByTitleOrSlug(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return Optional.empty();
        }

        List<Movie> movies = loadMovies();
        String searchSlug = Movie.generateSlug(searchTerm);

        // Recherche exacte par slug
        Optional<Movie> exactMatch = movies.stream()
                .filter(m -> m.getSlug().equalsIgnoreCase(searchSlug))
                .findFirst();

        if (exactMatch.isPresent()) {
            logger.info("Film trouvé (correspondance exacte) : {}", exactMatch.get().getSlug());
            return exactMatch;
        }

        // Recherche avec tolérance (distance de Levenshtein <= 3)
        Movie bestMatch = null;
        int bestDistance = Integer.MAX_VALUE;

        for (Movie movie : movies) {
            int distance = LevenshteinCalculator.calculate(searchSlug, movie.getSlug());
            if (distance <= 3 && distance < bestDistance) {
                bestDistance = distance;
                bestMatch = movie;
            }
        }

        if (bestMatch != null) {
            logger.info("Film trouvé (distance Levenshtein = {}) : {}", bestDistance, bestMatch.getSlug());
        } else {
            logger.info("Aucun film trouvé pour la recherche : '{}'", searchTerm);
        }

        return Optional.ofNullable(bestMatch);
    }

    @Override
    public boolean existsByTitle(String title) {
        return loadMovies().stream()
                .anyMatch(m -> m.getTitle().equalsIgnoreCase(title));
    }

    @Override
    public boolean existsBySlug(String slug) {
        return loadMovies().stream()
                .anyMatch(m -> m.getSlug().equalsIgnoreCase(slug));
    }

    @Override
    public void addMovie(Movie movie) {
        if (existsByTitle(movie.getTitle())) {
            throw new IllegalArgumentException("Un film avec ce titre existe déjà");
        }
        if (existsBySlug(movie.getSlug())) {
            throw new IllegalArgumentException("Un film avec ce slug existe déjà");
        }

        List<Movie> movies = loadMovies();
        movies.add(movie);
        saveMovies(movies);

        logger.info("Film ajouté : {}", movie.getSlug());
    }

    @Override
    public int getTotalMinutesToSchedule() {
        return loadMovies().stream()
                .mapToInt(m -> m.getDuration() * m.getSeances())
                .sum();
    }

    // --- Méthodes internes ---

    /**
     * Crée un fichier JSON vide avec la structure de base.
     */
    private void createEmptyJsonFile() {
        try (FileWriter fw = new FileWriter(jsonFile)) {
            fw.write("{\"movies\":[]}");
        } catch (IOException e) {
            logger.error("Erreur lors de la création du fichier JSON vide", e);
            throw new RuntimeException("Erreur lors de la création du fichier JSON", e);
        }
    }

    /**
     * Lit les films depuis un Reader JSON.
     *
     * @param reader le reader contenant le JSON
     * @return la liste des films lus
     */
    private List<Movie> readMoviesFromJson(Reader reader) {
        try {
            JsonObject root = gson.fromJson(reader, JsonObject.class);

            if (root == null) {
                return new ArrayList<>();
            }

            JsonArray moviesArray = root.getAsJsonArray("movies");

            // Vérifie que le contenu n'est pas null ni vide
            if (moviesArray == null || moviesArray.size() == 0) {
                return new ArrayList<>();
            }

            // Désérialisation directe en List<Movie> avec TypeToken
            Type movieListType = new TypeToken<List<Movie>>(){}.getType();
            List<Movie> movies = gson.fromJson(moviesArray, movieListType);

            return movies != null ? movies : new ArrayList<>();

        } catch (JsonSyntaxException e) {
            logger.warn("Format JSON invalide, retour d'une liste vide", e);
            return new ArrayList<>();
        }
    }
}