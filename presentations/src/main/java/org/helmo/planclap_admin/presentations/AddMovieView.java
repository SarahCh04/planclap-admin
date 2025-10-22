package org.helmo.planclap_admin.presentations;

import java.util.List;

/**
 * Interface définissant les interactions nécessaires pour encoder un film.
 * Le présentateur délègue toutes les interactions utilisateur à cette vue.
 */
public interface AddMovieView {

    /**
     * Demande à l'utilisateur de saisir le titre d'un film.
     *
     * @return le titre saisi (non vide), ou null si annulation
     */
    String promptTitle();

    /**
     * Demande à l'utilisateur de saisir la durée du film en minutes.
     *
     * @param maxDuration la durée maximale autorisée
     * @return la durée saisie, ou null si annulation
     */
    Integer promptDuration(int maxDuration);

    /**
     * Demande à l'utilisateur de saisir l'URL du poster.
     *
     * @return l'URL saisie (non vide), ou null si annulation
     */
    String promptPosterUrl();

    /**
     * Demande à l'utilisateur de saisir la description du film.
     *
     * @param maxLength longueur maximale autorisée
     * @return la description saisie (non vide), ou null si annulation
     */
    String promptDescription(int maxLength);

    /**
     * Demande à l'utilisateur de saisir les libellés Cinecheck.
     * L'utilisateur doit saisir au moins un libellé d'âge et peut ajouter
     * des libellés de contenu.
     *
     * @return la liste des libellés Cinecheck, ou null si annulation
     */
    List<String> promptCinechecks();

    /**
     * Demande à l'utilisateur de saisir le nombre de séances à planifier.
     *
     * @param min nombre minimum de séances
     * @param max nombre maximum de séances
     * @return le nombre de séances saisi, ou null si annulation
     */
    Integer promptSeances(int min, int max);

    /**
     * Affiche un message d'information à l'utilisateur.
     *
     * @param message le message à afficher
     */
    void showMessage(String message);

    /**
     * Affiche un message d'erreur à l'utilisateur.
     *
     * @param message le message d'erreur à afficher
     */
    void showError(String message);
}