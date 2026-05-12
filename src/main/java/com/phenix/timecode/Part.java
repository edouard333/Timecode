package com.phenix.timecode;

import com.phenix.timecode.exception.SameNumberPartException;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Une partie d'une timeline.<br>
 * ça peut être des bobines.<br>
 * Cela permet de faire des calcules convertir un TC continu en bobine et
 * versement.
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public final class Part {

    /**
     * Numéro de la partie.<br>
     * Définit l'ordre des éléments (d'une sorte de timeline).
     */
    private int numero;

    /**
     * Nom de la partie.
     */
    private String nom;

    /**
     * Durée en timecode de la partie.
     */
    @NotNull
    private Timecode duree;

    /**
     * Durée de la partie.
     */
    private int dureeImage;

    /**
     * Le start timecode de la partie.
     */
    private Timecode startTimecode;

    /**
     * Le start timecode en image de la partie.
     */
    private int startTimecodeImage;

    /**
     * Une partie.
     *
     * @param duree La durée de la partie.
     * @param startTC Le start TC de la partie.
     */
    public Part(@NotNull Timecode duree, @NotNull Timecode startTC) {
        this(0, null, duree, startTC);
    }

    /**
     * Une partie.
     *
     * @param numero Numéro de la partie.
     * @param duree La durée de la partie.
     * @param startTC Le start TC de la partie.
     */
    public Part(int numero, @NotNull Timecode duree, @NotNull Timecode startTC) {
        this(numero, null, duree, startTC);
    }

    /**
     * Une partie.
     *
     * @param numero Numéro de la partie.
     * @param nom Nom de la partie.
     * @param duree La durée de la partie.
     * @param startTC Le start TC de la partie.
     */
    public Part(int numero, String nom, @NotNull Timecode duree, @NotNull Timecode startTC) {
        this.numero = numero;
        this.nom = nom;
        this.duree = duree;
        this.startTimecode = startTC;

        this.startTimecodeImage = startTC.toImage();
        this.dureeImage = duree.toImage(true);
    }

    /**
     * Vérifie qu'il n'y a pas de numéro en double dans la liste.
     *
     * @param listePartie La liste à vérifier.
     *
     * @throws SameNumberPartException
     */
    public static void checkSameNumero(@NotNull List<Part> listePartie) throws SameNumberPartException {
        // Integer est "numero".
        Map<Integer, List<Part>> listePartieDuplique = new HashMap<Integer, List<Part>>();

        for (Part partie : listePartie) {
            // Si le numéro existe déjà, on ajoute la partie pour ce numéro.
            if (listePartieDuplique.containsKey(partie.numero)) {
                listePartieDuplique.get(partie.numero).add(partie);
            } // Sinon, on ajoute le numéro à la liste.
            else {
                listePartieDuplique.put(partie.numero, new ArrayList<Part>(List.of(partie)));
            }
        }

        // Si, on a d'office toutes les parties, on doit garder que les parties où le numéro contient plus parties.
        listePartieDuplique.entrySet().removeIf(entry -> entry.getValue().size() == 1);

        // Si la liste n'est pas vide, on doit retourner une erreur :
        if (!listePartieDuplique.isEmpty()) {
            throw new SameNumberPartException(listePartieDuplique);
        }
    }

    /**
     * Retourne une liste de bobine.<br>
     * L'ordre des durées définit la bobine : Numéro et start TC.<br>
     * <br>
     * La règle des starts TC :<br>
     * * Bobine 1 : <em>01:00:00:00</em><br>
     * * Bobine 2 : <em>02:00:00:00</em><br>
     * ...
     *
     * @param listeDuree Liste des durées des bobines.
     * @return Liste des bobines.
     */
    public static List<Part> createListReel(@NotNull Timecode[] listeDuree) {
        List<Part> listeReel = new ArrayList<Part>();

        for (int i = 0; i < listeDuree.length; i++) {
            listeReel.add(new Part(i + 1, "R" + digit(i + 1), listeDuree[i], new Timecode(digit(i + 1) + ":00:00:00", listeDuree[i].getFramerate())));
        }

        return listeReel;
    }

    /**
     * Représente un nombre en "digit".
     *
     * @deprecated Utiliser la fonction standard créée.
     *
     * @param valeur La valeur à convertir en digit (0-9).
     * @return String
     */
    @Deprecated
    @NotNull
    private static String digit(int valeur) {
        return (valeur <= 9) ? "0" + valeur : "" + valeur;
    }

    /**
     * Retourne la durée de la partie.
     *
     * @return La durée.
     */
    @NotNull
    public Timecode getDuree() {
        return this.duree;
    }

    /**
     * Retourne la durée de la partie en nombre d'images.
     *
     * @return La durée en nombre d'images.
     */
    public int getDureeImage() {
        return this.dureeImage;
    }

    /**
     * Retourne le nom de la partie.
     *
     * @return Le nom.
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Retourne le numéro de la partie.
     *
     * @return Le numéro.
     */
    public int getNumero() {
        return this.numero;
    }

    /**
     * Retourne le start timecode de la partie.
     *
     * @return Le start timecode.
     */
    @NotNull
    public Timecode getStartTimecode() {
        return this.startTimecode;
    }

    /**
     * Retourne le start timecode de la partie en nombre d'images.
     *
     * @return Le start timecode en nombre d'images.
     */
    @NotNull
    public int getStartTimecodeImage() {
        return this.startTimecodeImage;
    }

    /**
     * Sur base de l'ordre de la liste, on renumérote les éléments.
     *
     * @param listePartie la liste à renuméroter.
     */
    public static void renumberPart(@NotNull List<Part> listePartie) {
        for (int i = 0; i < listePartie.size(); i++) {
            listePartie.get(i).setNumero(i + 1);
        }
    }

    /**
     * Modifie le numéro de la partie.
     *
     * @param numero Le numéro.
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * Tri une liste de partie selon leur numéro.<br>
     * Ne génère pas d'erreur s'il y 2 fois le même numéro.
     *
     * @param listePartie La liste à trier.
     */
    public static void sort(@NotNull List<Part> listePartie) {
        listePartie.sort(Comparator.comparingInt(Part::getNumero));
    }
}
