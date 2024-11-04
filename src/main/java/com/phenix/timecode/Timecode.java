package com.phenix.timecode;

import jakarta.validation.constraints.NotNull;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Permet de gérer le timecode en SMPTE et en nombre d'images.<br>
 * Les framerates supportés :<br>
 * * <em>23,976</em> i/s<br>
 * * <em>24</em> i/s<br>
 * * <em>25</em> i/s<br>
 * * <em>29,976</em> NDF i/s<br>
 * * <em>30</em> i/s<br>
 * <br>
 * Note : pour l'instant, la class ne supporte pas en {@code String} de timecode
 * en drop-frame ("<em>HH:mm:ss;ii</em>").
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public final class Timecode {

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
     * Si le framerate (29,97is) est en drop-frame ou non.
     */
    private boolean drop_frame;

    /**
     * Framerate du timecode.
     */
    private double framerate;

    /**
     * Nombre d'images (totale) du programme.
     */
    private int nombre_image;

    /**
     * Où commence le programme.<br>
     * Sert pour les conversions de timecode.
     */
    private String timecode_debut = "00:00:00:00";

    /**
     * Si les informations de base ne sont pas définies.
     */
    private String is_null;

    /**
     * Si la valeur du timecode doit être calculé.<br>
     * Le seul a être calculé est pour quand on encode un nombre d'images.
     */
    private boolean doit_etre_calcule = false;

    /**
     * Construit un timecode "{@code null}".
     */
    public Timecode() {
        this.is_null = "-1";
        this.drop_frame = false;
    }

    /**
     * Construit un timecode sur base d'un {@code String}
     * ("<em>HH:mm:ss:ii</em>").
     *
     * @param timecode Le timecode sous forme de {@code String}
     * ("<em>HH:mm:ss:ii</em>").
     */
    public Timecode(@NotNull String timecode) {
        // Si le timecode contient un ";", alors c'est en drop-frame.
        this(timecode, -1, Timecode.isDropFrame(timecode));
    }

    /**
     * Construit un timecode sur base d'un {@code String} et d'un framerate.
     *
     * @param timecode Le timecode sous forme de {@code String}
     * ("<em>HH:mm:ss:ii</em>").
     * @param framerate Le framerate du timecode.
     */
    public Timecode(String timecode, @NotNull Framerate framerate) {
        this(timecode, framerate.getValeur(), framerate.getDropFrame());
    }

    /**
     * Construit un timecode sur base d'un {@code String} et d'un framerate.
     *
     * @param timecode Le timecode sous forme de {@code String}
     * ("<em>HH:mm:ss:ii</em>").
     * @param framerate Le framerate du timecode.
     */
    public Timecode(@NotNull String timecode, double framerate) {
        // Si le timecode contient un ";", alors c'est en drop-frame.
        this(timecode, framerate, Timecode.isDropFrame(timecode));
    }

    /**
     * Construit un timecode sur base d'un {@code String} et d'un framerate.
     *
     * @param timecode Le timecode sous forme de {@code String}
     * ("<em>HH:mm:ss:ii</em>").
     * @param framerate Le framerate du timecode.
     * @param drop_frame Si le timecode est en drop-frame ou non.
     */
    public Timecode(@NotNull String timecode, double framerate, boolean drop_frame) {
        try {
            Scanner sc = new Scanner(timecode);
            sc.useDelimiter(":");

            this.heure = sc.nextInt();
            this.minute = sc.nextInt();
            this.seconde = sc.nextInt();
            this.image = sc.nextInt();

            this.framerate = framerate;
            this.drop_frame = drop_frame;

            this.is_null = "";

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

        this.is_null = "";
        this.doit_etre_calcule = true;
    }

    /**
     * Construit un timecode en fonction de sa durée en nombre d'images et d'un
     * framerate.
     *
     * @param nombre_image Durée en nombre d'images.
     * @param framerate Framerate du timecode.
     */
    public Timecode(int nombre_image, @NotNull Framerate framerate) {
        this(nombre_image, framerate.getValeur(), framerate.getDropFrame());
    }

    /**
     * Construit un timecode en fonction de sa durée en nombre d'images et d'un
     * framerate.
     *
     * @param nombre_image Durée en nombre d'images.
     * @param framerate Framerate du timecode.
     */
    public Timecode(int nombre_image, double framerate) {
        this(nombre_image, framerate, false);
    }

    /**
     * Construit un timecode en fonction de sa durée en nombre d'images et d'un
     * framerate.
     *
     * @param nombre_image Durée en nombre d'images.
     * @param framerate Framerate du timecode.
     * @param drop_frame {@code true} si c'est un timecode drop frame.
     */
    public Timecode(int nombre_image, double framerate, boolean drop_frame) {
        this.nombre_image = nombre_image;
        this.framerate = framerate;
        this.drop_frame = drop_frame;

        this.nombreImageToInt();

        this.is_null = "";
        this.doit_etre_calcule = true;
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
        this(heure, minute, seconde, image, -1, false);
    }

    /**
     * Construit un timecode à l'aide de ses différentes valeurs.
     *
     * @param heure Heure du timecode.
     * @param minute Minute du timecode.
     * @param seconde Seconde du timecode.
     * @param image Image du timecode.
     * @param framerate Le framerate.
     */
    public Timecode(int heure, int minute, int seconde, int image, @NotNull Framerate framerate) {
        this(heure, minute, seconde, image, framerate.getValeur(), framerate.getDropFrame());
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
        this(heure, minute, seconde, image, framerate, false);
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
     * @param drop_frame {@code true} si c'est un timecode drop frame.
     */
    public Timecode(int heure, int minute, int seconde, int image, double framerate, boolean drop_frame) {
        this.heure = heure;
        this.minute = minute;
        this.seconde = seconde;
        this.image = image;
        this.framerate = framerate;
        this.drop_frame = drop_frame;
        this.is_null = "";
    }

    /**
     * Ajoute un certain nombre d'images.
     *
     * @param nombre_image Nombre d'images.
     */
    public void addFrame(int nombre_image) {
        this.nombre_image = this.toImage() + nombre_image;
        this.doit_etre_calcule = true;
    }

    /**
     * Change d'un framerate à l'autre.<br>
     * On doit spécifier le timecode début via
     * {@link Timecode#setStartTimecode(String) setStartTimecode(String)}.
     *
     * @param framerate Le nouveau framerate.
     *
     * @throws Exception Le timecode de début n'a pas été renseigné.
     */
    public void changeFramerate(Framerate framerate) throws Exception {
        changeFramerate(framerate.getValeur());
    }

    /**
     * Change d'un framerate à l'autre.<br>
     * On doit spécifier le timecode début via
     * {@link Timecode#setStartTimecode(String) setStartTimecode(String)}.
     *
     * @param framerate Le nouveau framerate.
     *
     * @throws Exception Le timecode de début n'a pas été renseigné.
     */
    public void changeFramerate(double framerate) throws Exception {
        if (this.timecode_debut == null || this.timecode_debut.isEmpty()) {
            throw new Exception("Le timecode de début n'a pas été renseigné.");
        }

        int image_utile = toImage() - new Timecode(this.timecode_debut, this.framerate).toImage();
        this.framerate = framerate;
        this.nombre_image = image_utile + new Timecode(this.timecode_debut, this.framerate).toImage();
        this.doit_etre_calcule = true;
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
    private String digit(int valeur) {
        return (valeur <= 9) ? "0" + valeur : "" + valeur;
    }

    /**
     * Gère à moitié le drop frame du 29,97is.
     *
     * @return Timecode en 29,97 DF.
     */
    @NotNull
    private String dropFrame() {
        int nombre_image_tmp = (this.heure * 60 * 60 * 30) + (this.minute * 60 * 30) + (this.seconde * 30) + this.image;

        int nombre_minute = (nombre_image_tmp / 1800) * 2;

        nombre_image_tmp += nombre_minute;
        //System.out.println("nombre_minute (add) : " + nombre_minute);

        int nombre_heure = (nombre_image_tmp / (1 * 60 * 60 * 30)) * 10;
        //System.out.println("nombre_heure (sous) : " + nombre_heure);
        nombre_image_tmp -= nombre_heure + (this.heure * 2);

        int framerate_tmp = this.getFramerateCalcule();

        // Heure :
        int heure_tmp = nombre_image_tmp / (60 * 60 * framerate_tmp);

        // Minute :
        int minute_image = nombre_image_tmp % (60 * 60 * framerate_tmp);
        int minute_tmp = minute_image / (60 * framerate_tmp);

        // Seconde :
        int seconde_image = minute_image % (60 * framerate_tmp);
        int seconde_tmp = seconde_image / framerate_tmp;

        // Image :
        int image_tmp = seconde_image % framerate_tmp;

        if (seconde_tmp == 0 && image_tmp == 0 && (nombre_image_tmp != 0) && (heure_tmp + minute_tmp + seconde_tmp + image_tmp != heure_tmp)) {
            image_tmp = 2;
        }

        return digit(heure_tmp) + ":" + digit(minute_tmp) + ":" + digit(seconde_tmp) + ";" + digit(image_tmp);
    }

    /**
     * Gère à moitié le drop frame du 29,97is.
     *
     * @return Timecode en 29,97 DF.
     */
    private int compenserDropFrame() {
        int nombre_image_tmp = (this.heure * 60 * 60 * 30) + (this.minute * 60 * 30) + (this.seconde * 30) + this.image;

        int compensation = (nombre_image_tmp / 1800) * 2;

        int nombre_heure = (nombre_image_tmp / (1 * 60 * 60 * 30)) * 10;

        if (nombre_heure > 0) {
            compensation -= nombre_heure + (this.heure * 2);
        }

        return compensation;
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
     * Retourne le framerate pour faire des calculs en interne.
     *
     * @return Retourne la valeur du framerate pour calculer le timecode.
     */
    private int getFramerateCalcule() {
        // Si c'est du 23,976 i/s :
        if (this.framerate == Framerate.F23976.getValeur()) {
            return 24;
        } // Si c'est du 24 i/s :
        else if (this.framerate == Framerate.F24.getValeur()) {
            return 24;
        } // Si c'est du 25 i/s :
        else if (this.framerate == Framerate.F25.getValeur()) {
            return 25;
        } // Si c'est du 29,97 i/s NDF, 29,76 DF ou du 30 i/s :
        else if (this.framerate == Framerate.F2997.getValeur() || this.framerate == Framerate.F2997ND.getValeur() || this.framerate == Framerate.F30.getValeur()) {
            return 30;
        } // Sinon, on tente une conversion en int :
        else {
            return (int) this.framerate;
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
     * Retourne si le timecode est drop-frame ou non.
     *
     * @return {@code true} si c'est drop-frame, sinon {@code false}.
     */
    public boolean isDropFrame() {
        return this.drop_frame;
    }

    /**
     * Retourne si un timecode en {@code String} est drop-frame ou non.
     *
     * @param timecode Le timecode en SMPT.
     * @return {@code true} si le timecode est drop-frame, sinon {@code false}.
     */
    public static boolean isDropFrame(@NotNull String timecode) {
        return timecode.contains(";");
    }

    /**
     * Encode la variable nombre_image en heure, minute, seconde, image.
     */
    private void nombreImageToInt() {
        int framerate_tmp = this.getFramerateCalcule();

        // Heure :
        this.heure = this.nombre_image / (60 * 60 * framerate_tmp);

        // Minute :
        int minute_image = this.nombre_image % (60 * 60 * framerate_tmp);
        this.minute = minute_image / (60 * framerate_tmp);

        // Seconde :
        int seconde_image = minute_image % (60 * framerate_tmp);
        this.seconde = seconde_image / framerate_tmp;

        // Image :
        this.image = seconde_image % framerate_tmp;
    }

    /**
     * Modifie si le timecode est drop-frame ou non.
     *
     * @param drop_frame La valeur du drop-frame.
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
    public void setStartTimecode(String timecode_debut) {
        this.timecode_debut = timecode_debut;
    }

    /**
     * Retourne la représentation en nombre d'images le timecode (depuis
     * "<em>00:00:00:00</em>").
     *
     * @return Le nombre d'images que représente un timecode.
     */
    public int toImage() {
        return this.toImage(false);
    }

    /**
     * Retourne la représentation en nombre d'images.
     *
     * @param image_utile Si {@code true} alors le nombre d'images en tenant
     * compte que des images utiles.<br>
     * Doit définir le timecode début via
     * {@link Timecode#setStartTimecode(String) setStartTimecode(String)}.
     * @return Le nombre d'images que représente le timecode.
     */
    public int toImage(boolean image_utile) {
        if (this.doit_etre_calcule) {
            this.nombreImageToInt();
            this.doit_etre_calcule = false;
        }

        if (this.is_null.isEmpty()) {
            return (this.heure * 60 * 60 * this.getFramerateCalcule()) + (this.minute * 60 * this.getFramerateCalcule()) + (this.seconde * this.getFramerateCalcule()) + (this.image)
                    - ((image_utile) ? new Timecode(this.timecode_debut, this.framerate).toImage() : 0) - (this.drop_frame ? compenserDropFrame() : 0);
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
    @NotNull
    public String toString() {
        if (doit_etre_calcule) {
            nombreImageToInt();
            doit_etre_calcule = false;
        }

        if (is_null.isEmpty()) {
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
