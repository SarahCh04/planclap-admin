package presentations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;
import org.helmo.planclap_admin.presentations.*;
import org.helmo.planclap_admin.domains.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoviePresenterTest {

    @Mock
    private MovieRepository mockRepository;

    @Mock
    private MovieView mockView;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDisplayMoviesWithValidData() {
        // Arrange
        List<Movie> movies = List.of(
                createTestMovie("Vaiana", 100, 1),
                createTestMovie("Dune : Deuxième Partie", 166, 1)
        );

        when(mockRepository.loadMovies()).thenReturn(movies);

        MoviePresenter presenter = new MoviePresenter(mockRepository, mockView);

        // Act
        presenter.displayMovies();

        // Assert
        // Capture les arguments passés à showMovies()
        ArgumentCaptor<List<MovieViewModel>> moviesCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> dateCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> heuresCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> minutesCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(mockView).showMovies(
                moviesCaptor.capture(),
                dateCaptor.capture(),
                heuresCaptor.capture(),
                minutesCaptor.capture()
        );

        // Vérifications
        List<MovieViewModel> displayedMovies = moviesCaptor.getValue();
        assertEquals(2, displayedMovies.size());
        assertEquals("Vaiana", displayedMovies.get(0).getTitle());
        assertEquals("Dune : Deuxième Partie", displayedMovies.get(1).getTitle());

        assertNotNull(dateCaptor.getValue());
        assertTrue(heuresCaptor.getValue() >= 0);
        assertTrue(minutesCaptor.getValue() >= 0);

        // Vérifie que displayError n'a PAS été appelé
        verify(mockView, never()).displayError(anyString());
    }

    @Test
    void testDisplayMoviesWithEmptyList() {
        // Arrange
        when(mockRepository.loadMovies()).thenReturn(new ArrayList<>());

        MoviePresenter presenter = new MoviePresenter(mockRepository, mockView);

        // Act
        presenter.displayMovies();

        // Assert
        ArgumentCaptor<List<MovieViewModel>> moviesCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Integer> heuresCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> minutesCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(mockView).showMovies(
                moviesCaptor.capture(),
                anyString(),
                heuresCaptor.capture(),
                minutesCaptor.capture()
        );

        assertTrue(moviesCaptor.getValue().isEmpty());
        assertEquals(0, heuresCaptor.getValue());
        assertEquals(0, minutesCaptor.getValue());
    }

    @Test
    void testDisplayMoviesWithRepositoryError() {
        // Arrange
        when(mockRepository.loadMovies()).thenThrow(new RuntimeException("Erreur simulée"));

        MoviePresenter presenter = new MoviePresenter(mockRepository, mockView);

        // Act
        presenter.displayMovies();

        // Assert
        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockView).displayError(errorCaptor.capture());

        String errorMessage = errorCaptor.getValue();
        assertTrue(errorMessage.contains("Impossible de charger les films"));

        // Vérifie que showMovies n'a PAS été appelé
        verify(mockView, never()).showMovies(anyList(), anyString(), anyInt(), anyInt());
    }

    // --- Méthode utilitaire pour créer des films de test ---

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