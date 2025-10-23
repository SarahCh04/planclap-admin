package org.helmo.planclap_admin.domains;

/**
 * Calcule la distance de Levenshtein entre deux chaînes
 * Cette distance représente le nombre minimum d'opérations (insertion, suppression, substitution)
 * nécessaires pour transformer une chaîne en une autre
 */
public class LevenshteinCalculator {

    /**
     * Calcule la distance de Levenshtein entre deux chaînes
     *
     * Exemples :
     * - distance("rocky", "rocky") = 0 (identiques)
     * - distance("godzilla-vs-kong", "godzilla-x-kong") = 3 (vs -> x = 2 ops + -)
     * - distance("kung-fu-panda", "kung-fu-panda-4") = 2 (ajout de "-4")
     *
     * @param s1 première chaîne
     * @param s2 deuxième chaîne
     * @return la distance de Levenshtein
     */
    public static int calculate(String s1, String s2) {
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("Les chaînes ne peuvent pas être null");
        }

        if (s1.isEmpty()) {
            return s2.length();
        }
        if (s2.isEmpty()) {
            return s1.length();
        }

        // Matrice de programmation dynamique
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        // Initialisation de la première ligne et colonne
        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        // Calcul de la distance
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1,      // suppression
                                dp[i][j - 1] + 1       // insertion
                        ),
                        dp[i - 1][j - 1] + cost    // substitution
                );
            }
        }

        return dp[s1.length()][s2.length()];
    }
}