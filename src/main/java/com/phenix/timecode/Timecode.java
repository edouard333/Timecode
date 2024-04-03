package com.phenix.timecode;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Permet de gérer le timecode en SMPTE et en nombre d'images.
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 * @version 1.0.3
 */
public class Timecode {

    /**
     * Heure du timecode.
     */
    private int heure;

    /**
     * Minute du timecode.
     */
    private int minute;

    /**
     * Seconde du timecode.
     */
    private int seconde;

    /**
     * Image du timecode.
     */
    private int image;

    /**
     * Si le framerate (29,97is) est en drop frame ou non.
     */
    private boolean drop_frame = false;

    /**
     * Framerate du timecode.
     */
    private double framerate;

    /**
     * Nombre d'images (totale) du programme.
     */
    private int nombre_image;

    /**
     * Où commence le programme.
     */
    private String timecode_debut = "00:00:00:00";

    /**
     * Si le ... n'est pas défini.
     */
    private String isNull;

    /**
     * Si la valeur du timecode doit être calculé.<br>
     * Le seul a être calculé est pour quand on encode un nombre d'images.
     */
    private boolean doitEtreCalcule = false;

    /**
     * Construit un timecode "null".
     */
    public Timecode() {
        this.isNull = "-1";
    }

    /**
     * Construit un timecode sur base d'un String ("HH:mm:ss:ii").
     *
     * @param timecode Timecode en String.
     */
    public Timecode(String timecode) {
        Scanner sc = new Scanner(timecode);
        sc.useDelimiter(":");

        this.heure = this.extractNombre(sc.next());
        this.minute = this.extractNombre(sc.next());
        this.seconde = this.extractNombre(sc.next());
        this.image = this.extractNombre(sc.next());

        this.isNull = "";

        sc.close();
    }

    /**
     * Construit un timecode sur base d'un String et d'un framerate.
     *
     * @param timecode Le timecode sous forme de String (""HH:mm:ss:ii").
     * @param framerate Le framerate du timecode.
     */
    public Timecode(String timecode, double framerate) {

        try {
            Scanner sc = new Scanner(timecode);
            sc.useDelimiter(":");

            this.heure = sc.nextInt();
            this.minute = sc.nextInt();
            this.seconde = sc.nextInt();
            this.image = sc.nextInt();

            this.framerate = framerate;

            this.isNull = "";

            sc.close();
        } catch (InputMismatchException exception) {
            throw new InputMismatchException("Le timecode n'est pas correctement formaté : " + timecode);
        }
    }

    /**
     * Construit un timecode en fonction de sa durée en nombre d'images.
     *
     * @param nombre_image Durée en nombre d'images.
     */
    public Timecode(int nombre_image) {
        this.nombre_image = nombre_image;

        this.isNull = "";
        this.doitEtreCalcule = true;
    }

    /**
     * Construit un timecode en fonction de sa durée en nombre d'images et d'un
     * framerate.
     *
     * @param nombre_image Durée en nombre d'images.
     * @param framerate Framerate du timecode.
     */
    public Timecode(int nombre_image, double framerate) {
        this.nombre_image = nombre_image;
        this.framerate = framerate;

        this.nombreImageToInt();

        this.isNull = "";
    }

    /**
     * Construit un timecode à l'aide de ses différentes valeurs.
     *
     * @param heure Heure du timecode.
     * @param minute Minute du timecode.
     * @param seconde Seconde du timecode.
     * @param image Image du timecode.
     */
    public Timecode(int heure, int minute, int seconde, int image) {
        this.heure = heure;
        this.minute = minute;
        this.seconde = seconde;
        this.image = image;
        this.isNull = "";
    }

    /**
     * Construit un timecode à l'aide de ses différentes valeurs et d'un
     * framerate.
     *
     * @param heure Heure du timecode.
     * @param minute Minute du timecode.
     * @param seconde Seconde du timecode.
     * @param image Image du timecode.
     * @param framerate Framerate du timecode.
     */
    public Timecode(int heure, int minute, int seconde, int image, double framerate) {
        this.heure = heure;
        this.minute = minute;
        this.seconde = seconde;
        this.image = image;
        this.framerate = framerate;
        this.isNull = "";
    }

    /**
     * Ajoute un certain nombre d'images.
     *
     * @param nombre_image Nombre d'images.
     */
    public void addFrame(int nombre_image) {
        this.nombre_image = this.toImage() + nombre_image;
        this.doitEtreCalcule = true;
    }

    /**
     * Change d'un framerate à l'autre.
     *
     * @param framerate Le nouveau framerate.
     * @throws java.lang.Exception Le timecode de début n'a pas été renseigné.
     */
    public void changeFramerate(double framerate) throws Exception {
        if (this.timecode_debut == null || this.timecode_debut.isEmpty()) {
            throw new Exception("Le timecode de début n'a pas été renseigné.");
        }

        int image_utile = toImage() - new Timecode(this.timecode_debut, this.framerate).toImage();
        this.framerate = framerate;
        this.nombre_image = image_utile + new Timecode(this.timecode_debut, this.framerate).toImage();
        this.doitEtreCalcule = true;
    }

    /**
     * Représente un nombre en "digit".
     *
     * @param valeur La valeur à convertir en digit (0-9).
     * @return String
     */
    private String digit(int valeur) {
        return (valeur <= 9) ? "0" + valeur : "" + valeur;
    }

    /**
     * Gère à moitié le drop frame du 29,97is.
     *
     * @return Timecode en 29,97 DF.
     */
    private String dropFrame() {
        int image_tmp = this.image;
        int seconde_tmp = this.seconde;
        int minute_tmp = this.minute;
        int heure_tmp = this.heure;

        // Drop frame que pour 29,97
        if (this.drop_frame) {
            int minute_ = (int) Math.floor((nombre_image - 1) / 29.97D / 60D);

            // Compense la valeur pour avoir le drop frame au bon endroit.
            if (minute_ >= 5 && minute_ <= 39) {
                minute_ = (int) Math.floor((nombre_image) / 29.97D / 60D);
            }

            // Compense la valeur pour avoir le drop frame au bon endroit.
            if (minute_ >= 40) {
                minute_ = (int) Math.floor((nombre_image - 1) / 29.97D / 60D);
            }

            image_tmp += ((minute_) * 2);

            image_tmp -= ((int) Math.floor(minute_ / 10D)) * 2;

            while (image_tmp > 29) {
                image_tmp -= 30;
                seconde_tmp++;
            }

            while (seconde_tmp > 59) {
                seconde_tmp -= 60;
                minute_tmp++;
            }

            while (minute_tmp > 59) {
                minute_tmp -= 60;
                heure_tmp++;
            }
        }
        return digit(heure_tmp) + ":" + digit(minute_tmp) + ":" + digit(seconde_tmp) + ":" + digit(image_tmp);
    }

    /**
     * Retourne le nombre en {@code int} dans le {@code String}.
     *
     * @param valeur Le nombre en {@code String}.
     * @return Retourne le {@code int}.
     */
    private int extractNombre(String valeur) {
        // valeur = valeur.replaceAll("^0+", ""); // Enlève UN ou + de zéro en début de ligne
        return Integer.parseInt(valeur);
    }

    /**
     * Retourne le framerate.
     *
     * @return Le framerate actuel.
     */
    public double getFramerate() {
        return this.framerate;
    }

    /**
     * Retourne le framerate pour faire des calculs.
     *
     * @return Retourne la valeur du framerate pour calculer le timecode.
     */
    private int getFramerateCalcule() {
        // Si c'est du 24is
        if (this.framerate == Framerate.F24.getValeur()) {
            return 24;
        } // Si c'est du 25is
        else if (this.framerate == Framerate.F25.getValeur()) {
            return 25;
        } // Si c'est du 23,976
        else if (this.framerate == Framerate.F23976.getValeur()) {
            return 24;
        } // Sinon, c'est du 29,97 (en NDF).
        else {
            return 30;
        }
    }

    /**
     * Retourne le timecode de début.
     *
     * @return Timecode de début.
     */
    public String getStartTimecode() {
        return this.timecode_debut;
    }

    /**
     * Encode la variable nombre_image en heure, minute, seconde, image.
     */
    private void nombreImageToInt() {

        int framerate_tmp = this.getFramerateCalcule();

        // Nombre minute :
        if (this.drop_frame) {
            int nombre_minute = ((nombre_image % (60 * 60 * framerate_tmp)) / (60 * framerate_tmp));
        }

        // Heure :
        this.heure = nombre_image / (60 * 60 * framerate_tmp);

        // Minute :
        int minute_image = nombre_image % (60 * 60 * framerate_tmp);
        this.minute = minute_image / (60 * framerate_tmp);

        // Seconde :
        int seconde_image = minute_image % (60 * framerate_tmp);
        this.seconde = seconde_image / framerate_tmp;

        // Image :
        this.image = seconde_image % framerate_tmp;

        // En cas de drop frame (29,76) :
        if (this.drop_frame && this.seconde == 0 && (this.image == 0 || this.image == 1)) {
            this.image = 2;
        }

    }

    /**
     * Modifie la valeur du drop frame.
     *
     * @param drop_frame La valeur du drop frame.
     */
    public void setDropFrame(boolean drop_frame) {
        this.drop_frame = drop_frame;
    }

    /**
     * Modifie le framerate.<br>
     * Pour changer de timecode (existant vers un autre), utiliser
     * {@link Timecode#changeFramerate(double) changeFramerate(double)}.
     *
     * @param framerate Le framerate.
     */
    public void setFramerate(double framerate) {
        this.framerate = framerate;
    }

    /**
     * Définit le timecode de début.
     *
     * @param timecode_debut Nombre d'images.
     */
    public void setStartTimeCode(String timecode_debut) {
        this.timecode_debut = timecode_debut;
    }

    /**
     * Retourne la représentation en nombre d'images le timecode.
     *
     * @return Le nombre d'images que représente un timecode.
     */
    public int toImage() {
        return this.toImage(false);
    }

    /**
     * Retourne la représentation en image en tenant compte (si {@code true}) que du nombre
     * d'images utile (depuis le start TC).
     *
     * @param image_utile Si {@code true} alors le nombre d'images qu'en image
     * utile.
     * @return Le nombre d'images que représente le timecode.
     */
    public int toImage(boolean image_utile) {
        if (this.doitEtreCalcule) {
            this.nombreImageToInt();
            this.doitEtreCalcule = false;
        }

        if (this.isNull.isEmpty()) {
            return (this.heure * 60 * 60 * this.getFramerateCalcule()) + (this.minute * 60 * this.getFramerateCalcule()) + (this.seconde * this.getFramerateCalcule()) + (this.image)
                    - ((image_utile) ? new Timecode(this.timecode_debut, this.framerate).toImage() : 0);
        } else {
            return -1;
        }
    }

    /**
     * Retourne le timecode en {@code String} sous la représentation SMPTE.
     *
     * @return Le timecode en {@code String}.
     */
    @Override
    public String toString() {
        if (doitEtreCalcule) {
            nombreImageToInt();
            doitEtreCalcule = false;
        }

        if (isNull.isEmpty()) {
            if (this.drop_frame) {
                return dropFrame();
            } else {
                return digit(this.heure) + ":" + digit(this.minute) + ":" + digit(this.seconde) + ":" + digit(this.image);
            }
        } else {
            return "-1";
        }
    }
}
