package com.phenix.timecode.exception;

import com.phenix.timecode.Part;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Exception si plusieurs fois le même numéro appartait dans une liste pour des
 * parties ({@link com.phenix.timecode.Part}).
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class SameNumberPartException extends Exception {

    /**
     * Liste des parties qui ont les mêmes numéros.
     */
    private final Map<Integer, List<Part>> listePartieDuplique;

    /**
     * Construit une {@link SameNumberPartException} avec la partie qui pose
     * problème.
     *
     * @param listePartieDuplique Une liste avec les parties dont les numéros
     * sont les mêmes.
     */
    public SameNumberPartException(Map<Integer, List<Part>> listePartieDuplique) {
        super(buildMessage(listePartieDuplique));

        this.listePartieDuplique = listePartieDuplique;
    }

    /**
     * Retourne la liste des parties qui posent soucis.
     *
     * @return La liste des parties qui posent soucis.
     */
    public Map<Integer, List<Part>> getListePartieDuplique() {
        return this.listePartieDuplique;
    }

    /**
     * Retourne le message d'erreur.
     *
     * @param listePartieDuplique Une liste avec les parties dont les numéros
     * sont les mêmes.
     * @return Le message d'erreur.
     */
    private static String buildMessage(Map<Integer, List<Part>> listePartieDuplique) {
        String message = "Des numéros sont en double : ";

        int nombreNumero = 0;

        for (Entry<Integer, List<Part>> entree : listePartieDuplique.entrySet()) {
            if (nombreNumero != 0) {
                message += ", ";
            }

            message += entree.getKey() + " (";

            int nombrePartie = 0;

            for (Part partie : entree.getValue()) {
                if (nombrePartie != 0) {
                    message += ", ";
                }

                message += "'" + partie.getNom() + "'";

                nombrePartie++;
            }

            message += ")";

            nombreNumero++;
        }

        return message;
    }
}
