package org.helmo.planclap_admin.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.helmo.planclap_admin.presentations.ConsultMovieView;
import org.helmo.planclap_admin.presentations.MovieViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Implémentation CLI de la vue pour consulter un film.
 *
 * <p>Cette vue interagit avec l'utilisateur via la console (ligne de commande)
 * pour permettre la consultation des détails d'un film.</p>
 *
 * <p>Elle utilise :
 * <ul>
 *   <li>un {@link BufferedReader} pour lire les entrées utilisateur ;</li>
 *   <li>un {@link PrintStream} pour afficher les sorties ;</li>
 *   <li>un logger Log4j pour journaliser les interactions.</li>
 * </ul></p>
 */
public class ConsultMovieCLIView implements ConsultMovieView {

    private static final Logger logger = LogManager.getLogger(ConsultMovieCLIView.class);

    private final BufferedReader in;
    private final PrintStream out;

    /**
     * Construit une nouvelle vue CLI pour la consultation de films.
     *
     * @param in le flux pour lire les entrées utilisateur
     * @param out le flux de sortie pour afficher les messages
     */
    public ConsultMovieCLIView(BufferedReader in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public String askForTitleOrSlug() {
        out.println();
        out.print("Titre ou slug du film : ");
        try {
            String input = in.readLine().trim();
            logger.info("Recherche du film : '{}'", input);
            return input;
        } catch (IOException e) {
            logger.error("Erreur lors de la lecture de l'entrée utilisateur", e);
            return "";
        }
    }

    @Override
    public void displayMovieDetails(MovieViewModel movie) {
        out.println();
        out.println(movie.getTitle());
        out.println(movie.getDescription());
        out.println("Durée : " + movie.getDurationHHMM());
        out.println("Cinecheck : " + movie.getCinechecks());
        out.println("URL du poster : " + movie.getPosterUrl());
        out.println("Séances à planifier : " + movie.getNbSeances());
        out.println();

        logger.info("Détails du film '{}' affichés", movie.getTitle());
    }

    @Override
    public void displayNoMatchFound() {
        out.println();
        out.println("Aucune correspondance trouvée");
        out.println();

        logger.info("Aucune correspondance trouvée pour la recherche");
    }
}