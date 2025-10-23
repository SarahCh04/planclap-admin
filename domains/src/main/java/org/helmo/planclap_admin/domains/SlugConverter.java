package org.helmo.planclap_admin.domains;

import java.text.Normalizer;

/**
 * Utilitaire pour convertir un texte en slug
 * Un slug est en minuscules, sans accents, avec des tirets à la place des espaces/ponctuation
 */
public class SlugConverter {

    /**
     * Convertit un texte en slug
     * Exemple : "Kung Fu Panda 4" -> "kung-fu-panda-4"
     *
     * @param texte le texte à convertir
     * @return le slug correspondant
     */
    public static String toSlug(String texte) {
        if (texte == null || texte.isBlank()) {
            return "";
        }

        // Supprimer les accents
        String sansAccents = Normalizer.normalize(texte, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Convertir en minuscules
        String minuscules = sansAccents.toLowerCase();

        // Remplacer les espaces et la ponctuation par des tirets
        String avecTirets = minuscules.replaceAll("[\\s\\p{Punct}]+", "-");

        // Supprimer les tirets en début et fin
        String nettoye = avecTirets.replaceAll("^-+|-+$", "");

        return nettoye;
    }
}