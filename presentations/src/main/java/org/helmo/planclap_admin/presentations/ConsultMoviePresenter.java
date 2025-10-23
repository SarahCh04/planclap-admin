package org.helmo.planclap_admin.presentations;

import org.helmo.planclap_admin.domains.*;

import java.util.List;
import java.util.Optional;

/**
 * Présentateur pour la consultation d'un film.
 *
 * <p>Ce présentateur gère la logique de recherche d'un film avec tolérance
 * aux fautes de frappe en utilisant la distance de Levenshtein.</p>
 *
 * <p>Responsabilités :
 * <ul>
 *   <li>Récupérer la saisie utilisateur via la vue ;</li>
 *   <li>Convertir la saisie en slug ;</li>
 *   <li>Rechercher le film avec tolérance (distance ≤ 3) ;</li>
 *   <li>Préparer les données pour l'affichage ;</li>
 *   <li>Déléguer l'affichage à la vue.</li>
 * </ul></p>
 */
public class ConsultMoviePresenter {

    private static final int MAX_LEVENSHTEIN_DISTANCE = 3;

    private final MovieRepository repository;
    private final ConsultMovieView view;

    /**
     * Construit un nouveau présentateur pour la consultation de films.
     *
     * @param repository le repository pour accéder aux films
     * @param view la vue pour interagir avec l'utilisateur
     */
    public ConsultMoviePresenter(MovieRepository repository, ConsultMovieView view) {
        this.repository = repository;
        this.view = view;
    }

    /**
     * Lance le processus de consultation d'un film.
     *
     * <p>Workflow :
     * <ol>
     *   <li>Demande la saisie du titre ou slug</li>
     *   <li>Convertit en slug</li>
     *   <li>Recherche avec tolérance</li>
     *   <li>Affiche les détails ou "aucune correspondance"</li>
     * </ol></p>
     */
    public void consult() {
        // 1. Demander la saisie
        String input = view.askForTitleOrSlug();

        // 2. Valider la saisie
        if (input == null || input.isBlank()) {
            view.displayNoMatchFound();
            return;
        }

        // 3. Convertir en slug
        String searchSlug = Movie.generateSlug(input);

        // 4. Rechercher avec tolérance
        Optional<Movie> foundMovie = searchWithTolerance(searchSlug);

        // 5. Afficher le résultat
        if (foundMovie.isPresent()) {
            MovieViewModel viewModel = createDetailViewModel(foundMovie.get());
            view.displayMovieDetails(viewModel);
        } else {
            view.displayNoMatchFound();
        }
    }

    /**
     * Recherche un film avec tolérance aux fautes de frappe.
     *
     * <p>Utilise la distance de Levenshtein pour trouver le film dont le slug
     * est le plus proche du slug recherché, avec une distance maximale de 3.</p>
     *
     * @param searchSlug le slug à rechercher
     * @return Optional contenant le film trouvé, ou vide si aucune correspondance
     */
    private Optional<Movie> searchWithTolerance(String searchSlug) {
        List<Movie> allMovies = repository.loadMovies();

        if (allMovies.isEmpty()) {
            return Optional.empty();
        }

        Movie bestMatch = null;
        int minDistance = Integer.MAX_VALUE;

        for (Movie movie : allMovies) {
            int distance = LevenshteinCalculator.calculate(searchSlug, movie.getSlug());

            // Ne retenir que les films avec distance ≤ 3
            // Et garder celui avec la distance minimale
            if (distance <= MAX_LEVENSHTEIN_DISTANCE && distance < minDistance) {
                minDistance = distance;
                bestMatch = movie;
            }
        }

        return Optional.ofNullable(bestMatch);
    }

    /**
     * Crée un MovieDetailViewModel à partir d'un Movie du domaine.
     *
     * @param movie le film du domaine
     * @return le modèle de vue pour l'affichage
     */
    private MovieViewModel createDetailViewModel(Movie movie) {
        String cinechecksFormatted = formatCinechecks(movie.getCinechecks());

        return new MovieViewModel(
                movie.getSlug(),
                movie.getTitle(),
                movie.getDurationHHMM(),
                movie.getPosterUrl(),
                movie.getDescription(),
                cinechecksFormatted,
                movie.getSeances()
        );
    }

    /**
     * Formate les cinechecks pour l'affichage.
     *
     * <p>Format : "âge [, contenu1, contenu2, ...]"</p>
     * <p>Exemples :
     * <ul>
     *   <li>["AL"] → "AL"</li>
     *   <li>["12", "Violence"] → "12 ans, Violence"</li>
     *   <li>["9", "Violence", "Peur"] → "9 ans, Violence, Peur"</li>
     * </ul></p>
     *
     * @param cinechecks la liste des libellés cinecheck
     * @return les cinechecks formatés
     */
    private String formatCinechecks(List<String> cinechecks) {
        if (cinechecks.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        // Le premier élément est l'âge
        String age = cinechecks.getFirst();
        if ("AL".equals(age)) {
            sb.append("AL");
        } else {
            sb.append(age).append(" ans");
        }

        // Ajouter les contenus s'ils existent
        for (int i = 1; i < cinechecks.size(); i++) {
            sb.append(", ").append(cinechecks.get(i));
        }

        return sb.toString();
    }
}