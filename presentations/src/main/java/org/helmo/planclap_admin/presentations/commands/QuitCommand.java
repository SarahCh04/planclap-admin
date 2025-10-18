package org.helmo.planclap_admin.presentations.commands;

/**
 * Commande CLI permettant de quitter l'application.
 * Cette commande affiche un message de fermeture à l'utilisateur
 * et termine immédiatement l'exécution du programme.
 */
public class QuitCommand implements CliCommand {

    /**
     * Exécute la commande de sortie.
     * Affiche un message indiquant que l'application se ferme puis
     * termine le programme avec un code de sortie 0.
     */
    @Override
    public void execute() {
        System.out.println("Fermeture de l'application...");
        System.exit(0);
    }
}
