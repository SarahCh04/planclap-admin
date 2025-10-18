package org.helmo.planclap_admin.presentations.commands;

import org.helmo.planclap_admin.presentations.MoviePresenter;

/**
 * Commande CLI permettant de lister les films disponibles.
 * Cette commande utilise un {@link MoviePresenter} pour récupérer et afficher
 * les films via la vue associée.
 */
public class ListMoviesCommand implements CliCommand {

    //Référence vers le présentateur des films utilisé pour afficher la liste
    private final MoviePresenter presenter;

    /**
     * Construit une commande de liste de films avec le présentateur spécifié
     *
     * @param presenter le présentateur chargé de récupérer et d'afficher les films
     */
    public ListMoviesCommand(MoviePresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Exécute la commande en appelant le présentateur pour afficher la liste des films.
     * Cette méthode délègue entièrement le travail à {@link MoviePresenter#displayMovies()}.
     */
    @Override
    public void execute() {
        presenter.displayMovies();
    }
}
