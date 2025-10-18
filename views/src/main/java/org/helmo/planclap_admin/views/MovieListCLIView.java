package org.helmo.planclap_admin.views;

import org.helmo.planclap_admin.presentations.MovieView;
import org.helmo.planclap_admin.presentations.MovieViewModel;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
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
     * Enfin, la méthode calcule et affiche la durée totale à planifier (somme des durées
     * de chaque film multipliée par son nombre de séances).
     * </p>
     *
     * @param movies = la liste des films à afficher,
     * si la liste est vide, un message indiquant qu’aucun film n’est à planifier est affiché.
     */
    @Override
    public void showMovies(List<MovieViewModel> movies) {
        // 1. Calcule du lundi de la semaine prochaine
        // On récupère la date du jour, puis on la déplace jusqu’au lundi suivant.
        LocalDate lundiProchain = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        // Formatage de la date au format dd/MM/yyyy pour affichage convivial.
        String dateFormattee = lundiProchain.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // 2. Titre de la section
        System.out.println("\n--- FILMS À PLANIFIER POUR LE " + dateFormattee + " ---");

        // 3. Gestion du cas où la liste est vide
        // Si aucun film n’est à planifier, on affiche un message spécifique et on quitte la méthode.
        if (movies.isEmpty()) {
            System.out.println("Aucun film à planifier.");
            System.out.println("Durée totale : 0 h 00");
            return;
        }

        // 4. Calcul de la durée totale TODO : ne devrait pas être dans la vue
        int totalMinutes = 0;

        // Pour chaque film, on affiche ses informations et on cumule la durée totale.
        for (MovieViewModel movie : movies) {

            // Affichage formaté : Titre - Durée - Nombre de séances
            System.out.printf("%s - %s - %d séances%n",
                    movie.getTitre(),
                    movie.getDureeHHMM(),
                    movie.getNbSeances());

            // --- Conversion de la durée en minutes ---
            // Le format attendu est "X h YY" → on sépare heures et minutes.
            String[] parts = movie.getDureeHHMM().split(" h ");
            int heures = Integer.parseInt(parts[0].trim());
            int minutes = Integer.parseInt(parts[1].trim());

            // On convertit en minutes et on multiplie par le nombre de séances prévues.
            totalMinutes += (heures * 60 + minutes) * movie.getNbSeances();
        }

        // --- Affichage de la durée totale ---
        // On reconvertit la durée totale en heures et minutes pour l’affichage final.
        int heures = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        // Affichage du résultat formaté avec un zéro devant les minutes si besoin.
        System.out.printf("Durée totale : %d h %02d%n", heures, minutes);
    }
}
