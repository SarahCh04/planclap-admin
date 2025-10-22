package org.helmo.planclap_admin.domains;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Représente un film à planifier dans le système PlanClap.
 * Cette classe du domaine encapsule toutes les informations nécessaires
 * pour gérer et planifier les séances d'un film.
 */
public class Movie {
    private final String slug;
    private final String title;
    private final int duration; // en minutes
    private final String posterUrl;
    private final String description;
    private final List<String> cinechecks;
    private final int seances;

    /**
     * Constructeur complet pour créer un film avec tous ses attributs.
     */
    public Movie(String slug, String title, int duration, String posterUrl,
                 String description, List<String> cinechecks, int seances) {
        this.slug = slug;
        this.title = title;
        this.duration = duration;
        this.posterUrl = posterUrl;
        this.description = description;
        this.cinechecks = new ArrayList<>(cinechecks);
        this.seances = seances;
    }

    /**
     * Constructeur simplifié qui génère automatiquement le slug à partir du titre.
     */
    public Movie(String title, int duration, String posterUrl,
                 String description, List<String> cinechecks, int seances) {
        this(generateSlug(title), title, duration, posterUrl, description, cinechecks, seances);
    }

    // Getters
    public String getSlug() { return slug; }
    public String getTitle() { return title; }
    public int getDuration() { return duration; }
    public String getPosterUrl() { return posterUrl; }
    public String getDescription() { return description; }
    public List<String> getCinechecks() { return new ArrayList<>(cinechecks); }
    public int getSeances() { return seances; }

    /**
     * Retourne la durée formatée au format HH:MM.
     */
    public String getDurationHHMM() {
        int h = duration / 60;
        int m = duration % 60;
        return String.format("%d h %02d", h, m);
    }

    /**
     * Génère un slug à partir d'un titre selon les règles :
     * - Supprime les accents
     * - Remplace les espaces et ponctuation par des tirets
     * - Convertit en minuscules
     * - Supprime les tirets multiples consécutifs
     */
    public static String generateSlug(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Le titre ne peut pas être vide");
        }
        // Suppression des accents (normalisation NFD puis suppression des caractères diacritiques)
        String normalized = Normalizer.normalize(title, Normalizer.Form.NFD);
        String withoutAccents = normalized.replaceAll("\\p{M}", "");
        // Conversion en minuscules
        String lowercase = withoutAccents.toLowerCase();
        // Remplacement des espaces et ponctuation par des tirets
        String slug = lowercase.replaceAll("[\\s\\p{Punct}]+", "-");
        // Suppression des tirets en début et fin
        slug = slug.replaceAll("^-+|-+$", "");
        return slug;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(slug, movie.slug);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slug);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                ", seances=" + seances +
                '}';
    }
}