package org.helmo.planclap_admin.domains;

import java.util.List;
import java.util.Optional;

/**
 * Interface définissant le contrat pour accéder et manipuler les films.
 * Cette abstraction permet de découpler le domaine des détails d'implémentation
 * du stockage (JSON, base de données, etc.).
 */
public interface MovieRepository {

    /**
     * Charge tous les films disponibles.
     *
     * @return une liste de films (vide si aucun film n'existe)
     */
    List<Movie> loadMovies();

    /**
     * Sauvegarde la liste complète des films.
     *
     * @param movies la liste des films à sauvegarder
     */
    void saveMovies(List<Movie> movies);

    /**
     * Recherche un film par son titre ou son slug.
     * La recherche peut être tolérante (distance de Levenshtein).
     *
     * @param searchTerm le titre ou slug à rechercher
     * @return un Optional contenant le film trouvé, ou vide si aucune correspondance
     */
    Optional<Movie> findByTitleOrSlug(String searchTerm);

    /**
     * Vérifie si un film avec ce titre existe déjà.
     *
     * @param title le titre à vérifier
     * @return true si un film avec ce titre existe
     */
    boolean existsByTitle(String title);

    /**
     * Vérifie si un film avec ce slug existe déjà.
     *
     * @param slug le slug à vérifier
     * @return true si un film avec ce slug existe
     */
    boolean existsBySlug(String slug);

    /**
     * Ajoute un nouveau film à la collection.
     *
     * @param movie le film à ajouter
     * @throws IllegalArgumentException si le film existe déjà (titre ou slug)
     */
    void addMovie(Movie movie);

    /**
     * Calcule le nombre total de minutes à planifier pour tous les films.
     *
     * @return le total en minutes (durée × nombre de séances pour chaque film)
     */
    int getTotalMinutesToSchedule();
}