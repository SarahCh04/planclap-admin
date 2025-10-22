package org.helmo.planclap_admin.infrastructures;

import com.google.gson.*;
import org.helmo.planclap_admin.domains.MovieRepository;
import org.helmo.planclap_admin.domains.Movie;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du repository de films utilisant un fichier JSON pour le stockage.
 * Cette classe gère la sérialisation/désérialisation des films et implémente
 * la recherche avec tolérance (distance de Levenshtein).
 */
public class JsonMovieRepository implements MovieRepository {

    private final File jsonFile;
    private final Gson gson;

    public JsonMovieRepository(File dir) {
        this.jsonFile = new File(dir, "movies.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public List<Movie> loadMovies() {
        if (!jsonFile.exists()) {
            createEmptyJsonFile();
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(jsonFile)) {
            return readMoviesFromJson(reader);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier JSON", e);
        }
    }

    @Override
    public void saveMovies(List<Movie> movies) {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            // Conversion des Movie en DTO pour la sérialisation
            List<MovieDTO> dtos = movies.stream()
                    .map(this::toDTO)
                    .toList();

            JsonObject root = new JsonObject();
            root.add("movies", gson.toJsonTree(dtos));

            gson.toJson(root, writer);
        } catch (IOException e) {
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
            return exactMatch;
        }

        // Recherche avec tolérance (distance de Levenshtein <= 3)
        Movie bestMatch = null;
        int bestDistance = Integer.MAX_VALUE;

        for (Movie movie : movies) {
            int distance = levenshteinDistance(searchSlug, movie.getSlug());
            if (distance <= 3 && distance < bestDistance) {
                bestDistance = distance;
                bestMatch = movie;
            }
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
    }

    @Override
    public int getTotalMinutesToSchedule() {
        return loadMovies().stream()
                .mapToInt(m -> m.getDuration() * m.getSeances())
                .sum();
    }

    // --- Méthodes privées ---

    private void createEmptyJsonFile() {
        try (FileWriter fw = new FileWriter(jsonFile)) {
            fw.write("{\"movies\":[]}");
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la création du fichier JSON", e);
        }
    }

    private List<Movie> readMoviesFromJson(Reader reader) {
        try {
            JsonObject root = gson.fromJson(reader, JsonObject.class);
            JsonArray moviesArray = root.getAsJsonArray("movies");
            //Vérifie que le contenu n'est pas null ni vide
            if (moviesArray == null || moviesArray.size() == 0) {
                return new ArrayList<>();
            }

            List<Movie> movies = new ArrayList<>();
            for (JsonElement element : moviesArray) {
                MovieDTO dto = gson.fromJson(element, MovieDTO.class);
                movies.add(fromDTO(dto));
            }
            return movies;

        } catch (JsonSyntaxException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Calcule la distance de Levenshtein entre deux chaînes.
     * Cette distance mesure le nombre minimum d'opérations (insertion, suppression, substitution)
     * nécessaires pour transformer une chaîne en une autre.
     */
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }

        return dp[s1.length()][s2.length()];
    }

    // --- Conversion DTO ---

    private MovieDTO toDTO(Movie movie) {
        MovieDTO dto = new MovieDTO();
        dto.slug = movie.getSlug();
        dto.title = movie.getTitle();
        dto.duration = movie.getDuration();
        dto.posterUrl = movie.getPosterUrl();
        dto.description = movie.getDescription();
        dto.cinechecks = new ArrayList<>(movie.getCinechecks());
        dto.seances = movie.getSeances();
        return dto;
    }

    private Movie fromDTO(MovieDTO dto) {
        return new Movie(
                dto.slug != null ? dto.slug : Movie.generateSlug(dto.title),
                dto.title,
                dto.duration,
                dto.posterUrl,
                dto.description,
                dto.cinechecks != null ? dto.cinechecks : new ArrayList<>(),
                dto.seances
        );
    }

    /**
     * DTO interne pour la sérialisation JSON.
     * Cette classe ne fait pas partie du domaine et reste confinée à l'infrastructure.
     */
    private static class MovieDTO {
        String slug;
        String title;
        int duration;
        String posterUrl;
        String description;
        List<String> cinechecks;
        int seances;
    }
}