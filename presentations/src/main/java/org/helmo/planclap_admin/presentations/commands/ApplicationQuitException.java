package org.helmo.planclap_admin.presentations.commands;

/**
 * Exception utilisée pour signaler une demande de fermeture de l'application
 * sans arrêter brutalement la JVM avec System.exit().
 *
 * <p>Elle est volontairement non vérifiée (hérite de RuntimeException)
 * afin de pouvoir être propagée librement jusqu'au point d'entrée
 * (la boucle principale du programme).</p>
 */
public class ApplicationQuitException extends RuntimeException {

    public ApplicationQuitException() {
        super("Fermeture de l'application demandée.");
    }
}
