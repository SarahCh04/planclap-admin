package org.helmo.planclap_admin.presentations;

/**
 * Interface de la vue pour consulter les détails d'un film.
 *
 * <p>Cette interface définit le contrat entre le présentateur et la vue
 * pour la fonctionnalité de consultation d'un film (US3).</p>
 *
 * <p>Elle permet de :
 * <ul>
 *   <li>demander à l'utilisateur de saisir un titre ou slug ;</li>
 *   <li>afficher les détails complets d'un film trouvé ;</li>
 *   <li>informer l'utilisateur qu'aucune correspondance n'a été trouvée.</li>
 * </ul></p>
 */
public interface ConsultMovieView {

    /**
     * Demande à l'utilisateur de saisir un titre ou un slug de film.
     *
     * @return le texte saisi par l'utilisateur (peut être vide ou null)
     */
    String askForTitleOrSlug();

    /**
     * Affiche les détails complets d'un film.
     *
     * @param movieDetail le modèle de vue contenant les détails du film à afficher
     */
    void displayMovieDetails(MovieViewModel movieDetail);

    /**
     * Affiche un message indiquant qu'aucune correspondance n'a été trouvée
     * pour la recherche effectuée.
     */
    void displayNoMatchFound();
}