package org.helmo.planclap_admin.presentations;

import org.helmo.planclap_admin.domains.Movie;
import org.helmo.planclap_admin.domains.MovieRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
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
        try{
            List<Movie> movies = repository.loadMovies();
            List<MovieViewModel> models = movies.stream()
                    .map(f -> new MovieViewModel(f.getTitle(), f.getDurationHHMM(), f.getSeances()))
                    .toList();
            // Calcul du total d'heures
            int totalMinutes = movies.stream()
                    .mapToInt(m -> m.getDuration() * m.getSeances())
                    .sum();
            int heures = totalMinutes / 60;
            int minutes = totalMinutes % 60;

            // Calcul du lundi de la semaine prochaine
            LocalDate lundiProchain = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            String dateFormattee = lundiProchain.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            // Envoie toutes les infos à la vue
            view.showMovies(models, dateFormattee, heures, minutes);
        }catch (Exception e){
            view.displayError("Impossible de charger les films !");
        }

    }
}
