package org.helmo.planclap_admin.presentations;

import org.helmo.planclap_admin.domains.Movie;
import org.helmo.planclap_admin.domains.MovieRepository;
import java.util.List;

/**
 * Cette classe fait le lien entre le repository de films (domaine) et la vue CLI ou autre vue implémentant
 * l'interface {@link MovieView}. Elle récupère les films depuis le repository et les convertit en
 * {@link MovieViewModel} avant de les envoyer à la vue pour affichage.
 */
public class MoviePresenter {

    //Référence vers le repository de films permettant de charger les données du domaine
    private final MovieRepository repository;
    //Référence vers la vue qui affichera les films
    private final MovieView view;

    /**
     * Construit un {@code MoviePresenter} avec le repository et la vue spécifiés.
     *
     * @param repository le repository fournissant les films
     * @param view la vue qui affichera les films
     */
    public MoviePresenter(MovieRepository repository, MovieView view) {
        this.repository = repository;
        this.view = view;
    }

    /**
     * Charge les films depuis le repository, les convertit en {@link MovieViewModel} et les transmet
     * à la vue pour affichage.
     * Chaque {@link MovieViewModel} contient le titre, la durée formatée en HH:MM et le nombre de séances
     * prévues pour le film.
     */
    public void displayMovies() {
        // Charge tous les films depuis le repository
        List<Movie> movies = repository.loadMovies();

        // Transforme chaque film en MovieViewModel pour la vue
        List<MovieViewModel> models = movies.stream()
                .map(f -> new MovieViewModel(f.getTitle(), f.getDurationHHMM(), f.getSeances()))
                .toList();

        // Envoie les modèles à la vue pour affichage
        view.showMovies(models);
    }
}
