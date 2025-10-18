package org.helmo.planclap_admin.presentations.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un menu CLI (Command Line Interface) permettant de gérer
 * plusieurs commandes (CliCommand) associées à des étiquettes (labels).
 * <p>Cette classe affiche un menu, lit le choix de l'utilisateur et exécute
 * la commande correspondante. Elle gère également l'option de quitter le menu</p>
 */
public class CommandMap implements CliCommand {

    //Flux d'entrée pour lire les choix de l'utilisateur
    private final BufferedReader cin;
    //Flux de sortie pour afficher le menu et les messages à l'utilisateur
    private final PrintStream cout;
    //Liste des étiquettes correspondant aux commandes disponibles
    private final List<String> labels = new ArrayList<>();
    //Liste des commandes associées aux étiquettes
    private final List<CliCommand> commands = new ArrayList<>();

    /**
     * Construit un nouveau menu CLI avec les flux d'entrée et de sortie spécifiés.
     *
     * @param cin le flux d'entrée pour lire les choix de l'utilisateur
     * @param cout le flux de sortie pour afficher le menu et les messages
     */
    public CommandMap(BufferedReader cin, PrintStream cout) {
        this.cin = cin;
        this.cout = cout;
    }

    /**
     * Ajoute un item au menu.
     *
     * @param label la description textuelle de la commande
     * @param command la commande à exécuter lorsque l'utilisateur sélectionne cet item
     */
    public void addItem(String label, CliCommand command) {
        labels.add(label);
        commands.add(command);
    }

    /**
     * Exécute la boucle principale du menu CLI.
     * <p>
     * Affiche le menu, lit le choix de l'utilisateur et exécute la commande correspondante.
     * Le menu continue jusqu'à ce que l'utilisateur choisisse de quitter.
     * </p>
     */
    @Override
    public void execute() {
        boolean quitRequested = false;
        while (!quitRequested) {
            displayItems();
            int choice = readChoice();

            if (choice == labels.size() + 1) {
                // L'utilisateur a choisi "Quitter"
                quitRequested = true;
            } else if (choice > 0 && choice <= labels.size()) {
                // Exécution de la commande sélectionnée
                commands.get(choice - 1).execute();
            } else {
                // Entrée invalide
                cout.println("Entrée inconnue");
            }
        }
    }

    /**
     * Affiche le menu des commandes disponibles avec leurs indices.
     * Chaque commande est numérotée, et une option "Quitter" est ajoutée à la fin.
     */
    private void displayItems() {
        cout.println("\n--- MENU PRINCIPAL ---");
        for (int i = 0; i < labels.size(); i++) {
            cout.printf("%d. %s%n", i + 1, labels.get(i));
        }
        cout.printf("%d. Quitter%n", labels.size() + 1);
    }

    /**
     * Lit le choix de l'utilisateur à partir du flux d'entrée.
     *
     * @return l'indice choisi par l'utilisateur (1-based) ou -1 en cas d'erreur de lecture ou de format
     */
    private int readChoice() {
        cout.print("Votre choix : ");
        try {
            String input = cin.readLine();
            return Integer.parseInt(input);
        } catch (IOException | NumberFormatException e) {
            // Retourne -1 si la lecture échoue ou si l'entrée n'est pas un nombre
            return -1;
        }
    }
}
