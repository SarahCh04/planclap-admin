package org.helmo.planclap_admin.presentations.commands;

import org.helmo.planclap_admin.presentations.*;

/**
 * Commande CLI permettant de consulter un film en détail.
 * Cette commande utilise un {@link ConsultMoviePresenter} pour afficher les détails
 */
public class ConsultMovieCommand implements CliCommand {

    //Référence vers le présentateur des films utilisé pour afficher la liste
    private final ConsultMoviePresenter presenter;

    /**
     * Construit une commande de liste de films avec le présentateur spécifié
     *
     * @param presenter le présentateur chargé de récupérer et d'afficher les films
     */
    public ConsultMovieCommand(ConsultMoviePresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Exécute la commande en appelant le présentateur pour afficher la liste des films.
     * Cette méthode délègue entièrement le travail à {@link ConsultMoviePresenter#consult()}.
     */
    @Override
    public void execute() {
        presenter.consult();
    }
}
