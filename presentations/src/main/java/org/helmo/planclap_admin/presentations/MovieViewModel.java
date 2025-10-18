package org.helmo.planclap_admin.presentations;

/**
 * {@code MovieViewModel} représente un modèle de données destiné à la vue (View)
 *
 * <p>Cette classe agit comme un objet de transfert (DTO de présentation) entre
 * le présentateur ({@code Presenter}) et la vue ({@code View}).</p>
 *
 * <p>Elle encapsule uniquement les informations nécessaires à l’affichage d’un film :
 * <ul>
 *   <li>le titre du film ;</li>
 *   <li>sa durée au format texte "H h MM" ;</li>
 *   <li>le nombre de séances à planifier.</li>
 * </ul></p>
 *
 * <p>Contrairement au domaine, ce modèle n’expose aucune logique métier ni validation :
 * il ne sert qu’à présenter des données déjà préparées par le présentateur.</p>
 */
public class MovieViewModel {
    private final String titre;
    private final String dureeHHMM;
    private final int nbSeances;

    public MovieViewModel(String titre, String dureeHHMM, int nbSeances) {
        this.titre = titre;
        this.dureeHHMM = dureeHHMM;
        this.nbSeances = nbSeances;
    }

    public String getTitre() { return titre; }
    public String getDureeHHMM() { return dureeHHMM; }
    public int getNbSeances() { return nbSeances; }
}
