package org.helmo.planclap_admin.presentations;

import java.util.*;
/**
 * {@code MovieViewModel} représente un modèle de données destiné à la vue (View)
 *
 * <p>Cette classe agit comme un objet de transfert (DTO de présentation) entre
 * le présentateur ({@code Presenter}) et la vue ({@code View}).</p>
 *
 * <p>Elle encapsule uniquement les informations nécessaires à l’affichage d’un film :
 * <ul>
 *   <li>le titre du film ;</li>
 *   <li>la description complète ;</li>
 *   <li>la durée formatée (HH h MM) ;</li>
 *   <li>les libellés Cinecheck formatés ;</li>
 *   <li>l'URL du poster ;</li>
 *   <li>le nombre de séances à planifier.</li>
 * </ul></p>
 *
 * <p>Contrairement au domaine, ce modèle n’expose aucune logique métier ni validation :
 * il ne sert qu’à présenter des données déjà préparées par le présentateur.</p>
 */
public class MovieViewModel {
    private final String slug;
    private final String title;
    private final String durationHHMM; // en minutes
    private final String posterUrl;
    private final String description;
    private final String cinechecks;
    private final int seances;

    /**
     * Constructeur complet pour créer un film avec tous ses attributs.
     */
    public MovieViewModel(String slug, String title, String duration, String posterUrl,
                 String description, String cinechecks, int seances) {
        this.slug = slug;
        this.title = title;
        this.durationHHMM = duration;
        this.posterUrl = posterUrl;
        this.description = description;
        this.cinechecks = cinechecks;
        this.seances = seances;
    }

    //Getters
    public String getSlug() { return slug; }
    public String getTitle() { return title; }
    public String getDurationHHMM() { return durationHHMM; }
    public String getPosterUrl() { return posterUrl; }
    public String getDescription() { return description; }
    public String getCinechecks() { return cinechecks; }
    public int getNbSeances() { return seances; }
}
