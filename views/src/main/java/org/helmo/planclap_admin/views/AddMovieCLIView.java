package org.helmo.planclap_admin.views;

import org.helmo.planclap_admin.presentations.AddMovieView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation CLI de la vue pour encoder un film.
 * Gère toutes les interactions en ligne de commande avec l'utilisateur.
 */
public class AddMovieCLIView implements AddMovieView {

    private final BufferedReader cin;
    private final PrintStream cout;

    public AddMovieCLIView(BufferedReader cin, PrintStream cout) {
        this.cin = cin;
        this.cout = cout;
    }

    @Override
    public String promptTitle() {
        cout.println("\n--- ENCODAGE D'UN NOUVEAU FILM ---");
        return readNonBlankString("Titre du film : ");
    }

    @Override
    public Integer promptDuration(int maxDuration) {
        return readInteger("Durée en minutes (max " + maxDuration + ") : ", 1, maxDuration);
    }

    @Override
    public String promptPosterUrl() {
        return readNonBlankString("URL du poster : ");
    }

    @Override
    public String promptDescription(int maxLength) {
        while (true) {
            String desc = readNonBlankString("Description (max " + maxLength + " caractères) : ");
            if (desc == null) return null;

            if (desc.length() > maxLength) {
                cout.println("Description trop longue (" + desc.length() + " caractères). Maximum : " + maxLength);
            } else {
                return desc;
            }
        }
    }

    @Override
    public List<String> promptCinechecks() {
        cout.println("\n--- CINECHECKS ---");
        cout.println("Libellés d'âge : AL, 6, 9, 12, 14, 16, 18");
        cout.println("Libellés de contenu : Violence, Peur, Sexe, Paroles grossières, ...");

        List<String> cinechecks = new ArrayList<>();

        // Libellé d'âge obligatoire
        String age = readNonBlankString("Libellé d'âge : ");
        if (age == null) return null;
        cinechecks.add(age.trim());

        // Si c’est "AL", on ne demande pas les libellés de contenu
        if (age.equalsIgnoreCase("AL")) {
            cout.println("Aucun libellé de contenu requis pour 'AL'.");
            return cinechecks;
        }

        // Libellés de contenu optionnels
        cout.println("Libellés de contenu (appuyez sur Entrée pour terminer) :");
        while (true) {
            cout.print("Libellé : ");
            try {
                String label = cin.readLine();
                if (label == null || label.isBlank()) {
                    break; // Fin de la saisie
                }
                cinechecks.add(label.trim());
            } catch (IOException e) {
                return null;
            }
        }

        return cinechecks;
    }

    @Override
    public Integer promptSeances(int min, int max) {
        return readInteger("Nombre de séances à planifier (" + min + "-" + max + ") : ", min, max);
    }

    @Override
    public void showMessage(String message) {
        cout.println("\n✓ " + message);
    }

    @Override
    public void showError(String message) {
        cout.println("\n✗ ERREUR : " + message);
    }

    // --- Méthodes utilitaires ---

    /**
     * Lit une chaîne non-vide depuis l'entrée standard.
     *
     * @param prompt le message à afficher
     * @return la chaîne saisie (non-vide) ou null en cas d'erreur/annulation
     */
    private String readNonBlankString(String prompt) {
        cout.print(prompt);
        try {
            String input = cin.readLine();
            if (input == null || input.isBlank()) {
                return null;
            }
            return input.trim();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Lit un entier dans une plage définie.
     *
     * @param prompt le message à afficher
     * @param min valeur minimale (incluse)
     * @param max valeur maximale (incluse)
     * @return l'entier saisi ou null en cas d'erreur/annulation
     */
    private Integer readInteger(String prompt, int min, int max) {
        while (true) {
            cout.print(prompt);
            try {
                String input = cin.readLine();
                if (input == null || input.isBlank()) {
                    return null;
                }

                int value = Integer.parseInt(input.trim());

                if (value < min || value > max) {
                    cout.println("Valeur hors limites. Veuillez entrer un nombre entre " + min + " et " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                cout.println("Veuillez entrer un nombre valide.");
            } catch (IOException e) {
                return null;
            }
        }
    }
}