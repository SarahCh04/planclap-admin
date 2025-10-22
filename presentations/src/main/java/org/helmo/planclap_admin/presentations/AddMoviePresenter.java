package org.helmo.planclap_admin.presentations;

import org.helmo.planclap_admin.domains.Movie;
import org.helmo.planclap_admin.domains.MovieRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Présentateur responsable de l'encodage d'un nouveau film.
 * Gère la validation des données et la logique métier avant d'ajouter le film au repository.
 */
public class AddMoviePresenter {

    private final MovieRepository repository;
    private final AddMovieView view;

    // Constantes métier
    private static final int MAX_DURATION = 240; // minutes
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final int MIN_SEANCES = 1;
    private static final int MAX_SEANCES = 9;
    private static final int MAX_TOTAL_HOURS = 77;

    public AddMoviePresenter(MovieRepository repository, AddMovieView view) {
        this.repository = repository;
        this.view = view;
    }

    /**
     * Lance le processus d'encodage d'un film.
     * Gère toutes les étapes : saisie, validation, vérification des contraintes.
     */
    public void addMovie() {
        try {
            // 1. Saisie et validation du titre
            String title = promptForUniqueTitle();
            if (title == null) {
                view.showMessage("Encodage annulé.");
                return;
            }

            // 2. Saisie de la durée
            Integer duration = view.promptDuration(MAX_DURATION);
            if (duration == null) {
                view.showMessage("Encodage annulé.");
                return;
            }

            // 3. Saisie de l'URL du poster
            String posterUrl = view.promptPosterUrl();
            if (posterUrl == null) {
                view.showMessage("Encodage annulé.");
                return;
            }

            // 4. Saisie de la description
            String description = view.promptDescription(MAX_DESCRIPTION_LENGTH);
            if (description == null) {
                view.showMessage("Encodage annulé.");
                return;
            }

            // 5. Saisie des Cinechecks
            List<String> cinechecks = view.promptCinechecks();
            if (cinechecks == null) {
                view.showMessage("Encodage annulé.");
                return;
            }

            // 6. Saisie du nombre de séances
            Integer seances = view.promptSeances(MIN_SEANCES, MAX_SEANCES);
            if (seances == null) {
                view.showMessage("Encodage annulé.");
                return;
            }

            // 7. Vérification de la contrainte de temps total
            int currentTotalMinutes = repository.getTotalMinutesToSchedule();
            int newMovieMinutes = duration * seances;
            int totalAfterAdd = currentTotalMinutes + newMovieMinutes;

            if (totalAfterAdd >= MAX_TOTAL_HOURS * 60) {
                view.showError("Dépassement du nombre d'heures à planifier (>= 77h). Encodage annulé.");
                return;
            }

            // 8. Création et ajout du film
            Movie newMovie = new Movie(title, duration, posterUrl, description, cinechecks, seances);
            repository.addMovie(newMovie);

            view.showMessage("Film '" + title + "' encodé avec succès !");

        } catch (Exception e) {
            view.showError("Erreur lors de l'encodage : " + e.getMessage());
        }
    }

    /**
     * Demande un titre unique à l'utilisateur.
     * Redemande tant que le titre ou le slug existe déjà.
     *
     * @return le titre validé, ou null si l'utilisateur annule
     */
    private String promptForUniqueTitle() {
        int maxAttempts = 5;
        int attempts = 0;

        while (attempts < maxAttempts) {
            String title = view.promptTitle();
            if (title == null) {
                return null; // Annulation
            }

            String slug = Movie.generateSlug(title);

            if (repository.existsByTitle(title)) {
                view.showError("Un film avec ce titre existe déjà. Veuillez en saisir un autre.");
                attempts++;
            } else if (repository.existsBySlug(slug)) {
                view.showError("Un film avec ce slug (" + slug + ") existe déjà. Veuillez saisir un autre titre.");
                attempts++;
            } else {
                return title;
            }
        }

        view.showError("Trop de tentatives infructueuses.");
        return null;
    }
}
