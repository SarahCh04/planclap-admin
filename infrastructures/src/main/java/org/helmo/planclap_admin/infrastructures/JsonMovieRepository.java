package org.helmo.planclap_admin.infrastructures;

import com.google.gson.*;
import org.helmo.planclap_admin.domains.MovieRepository;
import com.google.gson.reflect.TypeToken;
import org.helmo.planclap_admin.domains.Movie;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code JsonMovieRepository} est une implémentation concrète de l’interface
 * {@link MovieRepository} qui permet de charger les films depuis un fichier JSON
 *
 * <p>Cette classe est responsable de la lecture (ou de la création) du fichier <i>movies.json</i>
 * stocké dans le répertoire passé en paramètre
 */
public class JsonMovieRepository implements MovieRepository {

    //Référence vers le fichier JSON contenant la liste des films
    private final File jsonFile;
    //Instance unique du parseur Gson utilisée pour la sérialisation/désérialisation JSON
    private final Gson gson = new Gson();

    /**
     * Crée un nouveau {@code JsonMovieRepository} à partir d’un répertoire donné.
     *
     * @param dir le répertoire dans lequel se trouve (ou sera créé) le fichier movies.json
     */
    public JsonMovieRepository(File dir) {
        // Le fichier cible est "movies.json".
        this.jsonFile = new File(dir, "movies.json");
    }

    /**
     * Charge la liste des films depuis le fichier JSON.
     *
     * <p>Si le fichier n’existe pas encore, la méthode le crée avec un tableau vide
     * et retourne une liste vide.</p>
     *
     * @return une liste d’objets {@link Movie} représentant les films lus depuis le fichier JSON.
     *         Si le fichier est vide ou mal formé, une liste vide est renvoyée.
     * @throws RuntimeException si une erreur d’entrée/sortie survient pendant la lecture ou la création du fichier.
     */
    @Override
    public List<Movie> loadMovies() {
        // Cas 1 : le fichier JSON n’existe pas encore
        if (!jsonFile.exists()) {
            try (FileWriter fw = new FileWriter(jsonFile)) {
                // On initialise un objet JSON vide avec la clé "movies"
                fw.write("{\"movies\":[]}");
            } catch (IOException e) {
                // On relance une RuntimeException pour simplifier la gestion d’erreur côté appelant
                throw new RuntimeException(e);
            }
            // Retour d’une liste vide pour signaler qu’aucun film n’est disponible
            return new ArrayList<>();
        }
        // Cas 2 : le fichier existe déjà, on le lit
        try (Reader reader = new FileReader(jsonFile)) {
            return ReadJson(reader);
        } catch (IOException e) {
            // En cas de problème de lecture, on encapsule l’erreur dans une RuntimeException
            throw new RuntimeException(e);
        }
    }

    private List<Movie> ReadJson(Reader reader) {
        // On lit le contenu JSON racine du fichier
        JsonObject root = gson.fromJson(reader, JsonObject.class);
        // On récupère le tableau associé à la clé "movies"
        JsonArray moviesArray = root.getAsJsonArray("movies");
        // Si le tableau est absent ou vide → on renvoie une liste vide
        if (moviesArray == null) return new ArrayList<>();
        // Définition du type générique pour la désérialisation en List<Movie>
        Type listType = new TypeToken<List<Movie>>() {}.getType();
        // Conversion du tableau JSON en liste d’objets Movie
        return gson.fromJson(moviesArray, listType);
    }

}
