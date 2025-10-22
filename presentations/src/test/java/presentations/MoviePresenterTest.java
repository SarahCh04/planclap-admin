package presentations;

import org.junit.jupiter.api.Test;
import org.helmo.planclap_admin.presentations.*;
import org.helmo.planclap_admin.domains.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MoviePresenterTest { //TODO : simplifier avec mockito

    @Test
    void testDisplayMoviesWithValidData() {
        // Arrange
        List<Movie> movies = new ArrayList<>();
        movies.add(createTestMovie("Vaiana", 100, 1));
        movies.add(createTestMovie("Dune : Deuxième Partie", 166, 1));

        FakeMovieRepository repo = new FakeMovieRepository(movies);
        FakeMovieView view = new FakeMovieView();
        MoviePresenter presenter = new MoviePresenter(repo, view);

        // Act
        presenter.displayMovies();

        // Assert
        assertFalse(view.displayedMovies.isEmpty());
        assertEquals(2, view.displayedMovies.size());
        assertNotNull(view.displayedDate);
        assertTrue(view.totalHeures >= 0);
        assertTrue(view.totalMinutes >= 0);
    }

    @Test
    void testDisplayMoviesWithEmptyList() {
        // Arrange
        FakeMovieRepository repo = new FakeMovieRepository(new ArrayList<>());
        FakeMovieView view = new FakeMovieView();
        MoviePresenter presenter = new MoviePresenter(repo, view);

        // Act
        presenter.displayMovies();

        // Assert
        assertTrue(view.displayedMovies.isEmpty());
        assertEquals(0, view.totalHeures);
        assertEquals(0, view.totalMinutes);
    }

    @Test
    void testDisplayMoviesWithRepositoryError() {
        // Arrange
        FakeMovieRepository repo = new FakeMovieRepository(true); // Simule une erreur
        FakeMovieView view = new FakeMovieView();
        MoviePresenter presenter = new MoviePresenter(repo, view);

        // Act
        presenter.displayMovies();

        // Assert
        assertNotNull(view.errorMessage);
        assertTrue(view.errorMessage.contains("Impossible de charger les films"));
    }

    // --- Classes de test (Fakes) ---

    /**
     * Fake Repository pour les tests.
     * Implémente toutes les méthodes de MovieRepository.
     */
    static class FakeMovieRepository implements MovieRepository {
        private final List<Movie> movies;
        private final boolean throwError;

        public FakeMovieRepository(List<Movie> movies) {
            this.movies = movies;
            this.throwError = false;
        }

        public FakeMovieRepository(boolean throwError) {
            this.movies = new ArrayList<>();
            this.throwError = throwError;
        }

        @Override
        public List<Movie> loadMovies() {
            if (throwError) {
                throw new RuntimeException("Erreur simulée");
            }
            return new ArrayList<>(movies);
        }

        @Override
        public void saveMovies(List<Movie> movies) {
            this.movies.clear();
            this.movies.addAll(movies);
        }

        @Override
        public Optional<Movie> findByTitleOrSlug(String searchTerm) {
            return movies.stream()
                    .filter(m -> m.getTitle().equalsIgnoreCase(searchTerm) ||
                            m.getSlug().equalsIgnoreCase(searchTerm))
                    .findFirst();
        }

        @Override
        public boolean existsByTitle(String title) {
            return movies.stream().anyMatch(m -> m.getTitle().equalsIgnoreCase(title));
        }

        @Override
        public boolean existsBySlug(String slug) {
            return movies.stream().anyMatch(m -> m.getSlug().equalsIgnoreCase(slug));
        }

        @Override
        public void addMovie(Movie movie) {
            movies.add(movie);
        }

        @Override
        public int getTotalMinutesToSchedule() {
            return movies.stream()
                    .mapToInt(m -> m.getDuration() * m.getSeances())
                    .sum();
        }
    }

    /**
     * Fake View pour capturer les appels.
     */
    static class FakeMovieView implements MovieView {
        List<MovieViewModel> displayedMovies = new ArrayList<>();
        String displayedDate;
        int totalHeures;
        int totalMinutes;
        String errorMessage;

        @Override
        public void showMovies(List<MovieViewModel> movies, String date, int heures, int minutes) {
            this.displayedMovies = movies;
            this.displayedDate = date;
            this.totalHeures = heures;
            this.totalMinutes = minutes;
        }

        @Override
        public void displayError(String message) {
            this.errorMessage = message;
        }
    }

    // --- Méthode utilitaire pour créer des films de test ---

    /**
     * Crée un film de test avec des valeurs par défaut pour les champs non essentiels.
     */
    private static Movie createTestMovie(String title, int duration, int seances) {
        return new Movie(
                title,
                duration,
                "https://example.com/poster.jpg",
                "Description de test",
                List.of("AL"),
                seances
        );
    }
}