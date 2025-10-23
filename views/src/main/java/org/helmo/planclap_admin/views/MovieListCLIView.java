package org.helmo.planclap_admin.views;

import org.helmo.planclap_admin.presentations.*;
import java.util.List;


/**
 * {@code MovieListCLIView} est une implémentation de l’interface {@link MovieView}
 * permettant d’afficher dans la console la liste des films à planifier pour la
 * semaine suivante.
 *
 * <p>Elle affiche :
 * <ul>
 *   <li>Le titre de la section avec la date du lundi de la semaine prochaine.</li>
 *   <li>La liste des films (titre, durée et nombre de séances à planifier).</li>
 *   <li>La durée totale à planifier pour la semaine.</li>
 * </ul></p>
 *
 * <p>Si aucun film n’est à planifier, elle affiche un message indiquant qu’aucun film
 * n’est disponible et une durée totale de 0 h 00.</p>
 */
public class MovieListCLIView implements MovieView {
    /**
     * Affiche dans la console la liste des films à planifier pour le lundi de la semaine prochaine.
     * <p>
     * Pour chaque film, les informations suivantes sont affichées :
     * <ul>
     *   <li>Le titre du film</li>
     *   <li>Sa durée au format "H h MM"</li>
     *   <li>Le nombre de séances à planifier</li>
     * </ul>
     * </p>
     * <p>
     * Enfin, la méthode affiche la durée totale à planifier (somme des durées
     * de chaque film multipliée par son nombre de séances).
     * </p>
     */
    @Override
    public void showMovies(List<MovieViewModel> movies, String date, int totalHeures, int totalMinutes) {
        System.out.println("\n--- FILMS À PLANIFIER POUR LE " + date + " ---");

        if (movies.isEmpty()) {
            System.out.println("Aucun film à planifier.");
            System.out.println("Durée totale : 0 h 00");
            return;
        }

        for (MovieViewModel movie : movies) {
            System.out.printf("%s - %s - %d séances%n",
                    movie.getTitle(),
                    movie.getDurationHHMM(),
                    movie.getNbSeances());
        }

        System.out.printf("Durée totale : %d h %02d%n", totalHeures, totalMinutes);
    }

    @Override
    public void displayError(String message) {
        System.out.println("Erreur : " + message);
    }


}
