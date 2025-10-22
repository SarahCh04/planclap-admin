package org.helmo.planclap_admin.presentations.commands;

import org.helmo.planclap_admin.presentations.AddMoviePresenter;

/**
 * Commande CLI permettant d'encoder un nouveau film.
 * Délègue l'exécution au présentateur AddMoviePresenter.
 */
public class AddMovieCommand implements CliCommand {

    private final AddMoviePresenter presenter;

    /**
     * Construit une commande d'ajout de film.
     *
     * @param presenter le présentateur gérant la logique d'encodage
     */
    public AddMovieCommand(AddMoviePresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Exécute la commande en déléguant au présentateur.
     */
    @Override
    public void execute() {
        presenter.addMovie();
    }
}