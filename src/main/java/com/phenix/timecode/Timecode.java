package com.phenix.timecode;

import com.phenix.timecode.exception.TimecodeException;
import com.phenix.timecode.exception.TimecodeRuntimeException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
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
 * Note : pour l'instant, la class ne supporte pas en {@link String} de timecode
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
    private boolean dropFrame;

    /**
     * Framerate du timecode.
     */
    private double framerate;

    /**
     * Nombre d'images (totale) du programme.
     */
    private int nombreImage;

    /**
     * Où commence le programme.<br>
     * Sert pour les conversions de timecode.
     */
    private String timecodeDebut = "00:00:00:00";

    /**
     * Si les informations de base ne sont pas définies.
     */
    private String isNull;

    /**
     * Si la valeur du timecode doit être calculé.<br>
     * Le seul a être calculé est pour quand on encode un nombre d'images.
     */
    private boolean doitEtreCalcule = false;

    /**
     * Construit un timecode "{@code null}".
     */
    public Timecode() {
        this.isNull = "-1";
        this.dropFrame = false;
    }

    /**
     * Construit un timecode sur base d'un {@link String}
     * ("<em>HH:mm:ss:ii</em>").
     *
     * @param timecode Le timecode sous forme de {@link String}
     * ("<em>HH:mm:ss:ii</em>").
     */
    public Timecode(@NotNull @NotBlank String timecode) {
        // Si le timecode contient un ";", alors c'est en drop-frame.
        this(timecode, -1, Timecode.isDropFrame(timecode));
    }

    /**
     * Construit un timecode sur base d'un {@link String} et d'un framerate.
     *
     * @param timecode Le timecode sous forme de {@link String}
     * ("<em>HH:mm:ss:ii</em>").
     * @param framerate Le framerate du timecode.
     */
    public Timecode(@NotNull @NotBlank String timecode, @NotNull Framerate framerate) {
        this(timecode, framerate.value, framerate.dropFrame);
    }

    /**
     * Construit un timecode sur base d'un {@link String} et d'un framerate.
     *
     * @param timecode Le timecode sous forme de {@link String}
     * ("<em>HH:mm:ss:ii</em>").
     * @param framerate Le framerate du timecode.
     */
    public Timecode(@NotNull @NotBlank String timecode, double framerate) {
        // Si le timecode contient un ";", alors c'est en drop-frame.
        this(timecode, framerate, Timecode.isDropFrame(timecode));
    }

    /**
     * Construit un timecode sur base d'un {@link String} et d'un framerate.
     *
     * @param timecode Le timecode sous forme de {@link String}
     * ("<em>HH:mm:ss:ii</em>").
     * @param framerate Le framerate du timecode.
     * @param dropFrame Si le timecode est en drop-frame ou non.
     */
    public Timecode(@NotNull @NotBlank String timecode, double framerate, boolean dropFrame) {
        try {
            Scanner sc = new Scanner(timecode);
            sc.useDelimiter(":");

            this.heure = sc.nextInt();
            this.minute = sc.nextInt();
            this.seconde = sc.nextInt();
            this.image = sc.nextInt();

            this.framerate = framerate;
            this.dropFrame = dropFrame;

            this.isNull = "";

            sc.close();
        } catch (InputMismatchException exception) {
            throw new TimecodeRuntimeException("Le timecode n'est pas correctement formaté : " + timecode, exception);
        } catch (NoSuchElementException exception) {
            throw new TimecodeRuntimeException("Le timecode n'est pas correctement formaté : " + timecode, exception);
        }
    }

    /**
     * Construit un timecode en fonction de sa durée en nombre d'images.
     *
     * @param nombreImage Durée en nombre d'images.
     */
    public Timecode(int nombreImage) {
        this.nombreImage = nombreImage;

        this.isNull = "";
        this.doitEtreCalcule = true;
    }

    /**
     * Construit un timecode en fonction de sa durée en nombre d'images et d'un
     * framerate.
     *
     * @param nombreImage Durée en nombre d'images.
     * @param framerate Framerate du timecode.
     */
    public Timecode(int nombreImage, @NotNull Framerate framerate) {
        this(nombreImage, framerate.value, framerate.dropFrame);
    }

    /**
     * Construit un timecode en fonction de sa durée en nombre d'images et d'un
     * framerate.
     *
     * @param nombreImage Durée en nombre d'images.
     * @param framerate Framerate du timecode.
     */
    public Timecode(int nombreImage, double framerate) {
        this(nombreImage, framerate, false);
    }

    /**
     * Construit un timecode en fonction de sa durée en nombre d'images et d'un
     * framerate.
     *
     * @param nombreImage Durée en nombre d'images.
     * @param framerate Framerate du timecode.
     * @param dropFrame {@code true} si c'est un timecode drop frame.
     */
    public Timecode(int nombreImage, double framerate, boolean dropFrame) {
        this.nombreImage = nombreImage;
        this.framerate = framerate;
        this.dropFrame = dropFrame;

        this.nombreImageToInt();

        this.isNull = "";
        this.doitEtreCalcule = true;
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
        this(heure, minute, seconde, image, framerate.value, framerate.dropFrame);
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
     * @param dropFrame {@code true} si c'est un timecode drop frame.
     */
    public Timecode(int heure, int minute, int seconde, int image, double framerate, boolean dropFrame) {
        this.heure = heure;
        this.minute = minute;
        this.seconde = seconde;
        this.image = image;
        this.framerate = framerate;
        this.dropFrame = dropFrame;
        this.isNull = "";
    }

    /**
     * Ajoute un certain nombre d'images.
     *
     * @param nombreImage Nombre d'images.
     */
    public void addFrame(int nombreImage) {
        this.nombreImage = this.toImage() + nombreImage;
        this.doitEtreCalcule = true;
    }

    /**
     * Change d'un framerate à l'autre.<br>
     * On doit spécifier le timecode début via
     * {@link #setStartTimecode(String)}.
     *
     * @param framerate Le nouveau framerate.
     *
     * @throws TimecodeException Le timecode de début n'a pas été renseigné.
     */
    public void changeFramerate(Framerate framerate) throws TimecodeException {
        this.changeFramerate(framerate.value);
    }

    /**
     * Change d'un framerate à l'autre.<br>
     * On doit spécifier le timecode début via
     * {@link #setStartTimecode(String)}.
     *
     * @param framerate Le nouveau framerate.
     *
     * @throws TimecodeException Le timecode de début n'a pas été renseigné.
     */
    public void changeFramerate(double framerate) throws TimecodeException {
        if (this.timecodeDebut == null || this.timecodeDebut.isBlank()) {
            throw new TimecodeException("Le timecode de début n'a pas été renseigné.");
        }

        int imageUtile = toImage() - new Timecode(this.timecodeDebut, this.framerate).toImage();
        this.framerate = framerate;
        this.nombreImage = imageUtile + new Timecode(this.timecodeDebut, this.framerate).toImage();
        this.doitEtreCalcule = true;
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
    @NotBlank
    private String dropFrame() {
        int nombreImageTmp = (this.heure * 60 * 60 * 30) + (this.minute * 60 * 30) + (this.seconde * 30) + this.image;

        int nombreMinute = (nombreImageTmp / 1800) * 2;
        nombreImageTmp += nombreMinute;

        int nombreHeure = (nombreImageTmp / (1 * 60 * 60 * 30)) * 10;
        nombreImageTmp -= nombreHeure + (this.heure * 2);

        int framerateTmp = this.getFramerateCalcule();

        // Heure :
        int heureTmp = nombreImageTmp / (60 * 60 * framerateTmp);

        // Minute :
        int minuteImage = nombreImageTmp % (60 * 60 * framerateTmp);
        int minuteTmp = minuteImage / (60 * framerateTmp);

        // Seconde :
        int secondeImage = minuteImage % (60 * framerateTmp);
        int secondeTmp = secondeImage / framerateTmp;

        // Image :
        int imageTmp = secondeImage % framerateTmp;

        if (secondeTmp == 0 && imageTmp == 0 && (nombreImageTmp != 0) && (heureTmp + minuteTmp + secondeTmp + imageTmp != heureTmp)) {
            imageTmp = 2;
        }

        return digit(heureTmp) + ":" + digit(minuteTmp) + ":" + digit(secondeTmp) + ";" + digit(imageTmp);
    }

    /**
     * Retourne si le timecode est dans l'interval donné en paramètre.
     *
     * @param interval L'interval.
     * @return {@code true} si le timecode est dans l'interval.
     */
    public boolean entre(IntervalTimecode interval) {
        return entre(this, interval.getTimecodeIn(), interval.getTimecodeOut());
    }

    /**
     * Retourne si le timecode est dans l'interval des timecodes in et out.
     *
     * @param tcIn Timecode in.
     * @param tcOut Timecode out.
     * @return {@code true} si le timecode est dans l'interval.
     */
    public boolean entre(Timecode tcIn, Timecode tcOut) {
        return entre(this, tcIn, tcOut);
    }

    /**
     * Retourne si le timecode est dans l'interval des timecodes in et out.
     *
     * @param tc Le timecode à vérifier.
     * @param tcIn Timecode in.
     * @param tcOut Timecode out.
     * @return {@code true} si le timecode est dans l'interval.
     */
    public static boolean entre(@NotNull Timecode tc, @NotNull Timecode tcIn, @NotNull Timecode tcOut) {
        return (tcIn.toImage() <= tc.toImage() && tc.toImage() <= tcOut.toImage());
    }

    /**
     * Gère à moitié le drop frame du 29,97is.
     *
     * @return Timecode en 29,97 DF.
     */
    private int compenserDropFrame() {
        int nombreImageTmp = (this.heure * 60 * 60 * 30) + (this.minute * 60 * 30) + (this.seconde * 30) + this.image;

        int compensation = (nombreImageTmp / 1800) * 2;

        int nombreHeure = (nombreImageTmp / (1 * 60 * 60 * 30)) * 10;

        if (nombreHeure > 0) {
            compensation -= nombreHeure + (this.heure * 2);
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
     * @return La valeur du framerate pour calculer le timecode.
     */
    private int getFramerateCalcule() {
        // Si c'est du 23,976 i/s :
        if (this.framerate == Framerate.F23976.value) {
            return 24;
        } // Si c'est du 24 i/s :
        else if (this.framerate == Framerate.F24.value) {
            return 24;
        } // Si c'est du 25 i/s :
        else if (this.framerate == Framerate.F25.value) {
            return 25;
        } // Si c'est du 29,97 i/s NDF, 29,76 DF ou du 30 i/s :
        else if (this.framerate == Framerate.F2997.value || this.framerate == Framerate.F2997ND.value || this.framerate == Framerate.F30.value) {
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
        return this.timecodeDebut;
    }

    /**
     * Retourne si le timecode est drop-frame ou non.
     *
     * @return {@code true} si c'est drop-frame, sinon {@code false}.
     */
    public boolean isDropFrame() {
        return this.dropFrame;
    }

    /**
     * Retourne si un timecode en {@link String} est drop-frame ou non.
     *
     * @param timecode Le timecode en SMPT.
     * @return {@code true} si le timecode est drop-frame, sinon {@code false}.
     */
    public static boolean isDropFrame(@NotNull String timecode) {
        return timecode.contains(";");
    }

    /**
     * Encode la variable nombreImage en heure, minute, seconde, image.
     */
    private void nombreImageToInt() {
        int framerateTmp = this.getFramerateCalcule();

        // Heure :
        this.heure = this.nombreImage / (60 * 60 * framerateTmp);

        // Minute :
        int minuteImage = this.nombreImage % (60 * 60 * framerateTmp);
        this.minute = minuteImage / (60 * framerateTmp);

        // Seconde :
        int secondeImage = minuteImage % (60 * framerateTmp);
        this.seconde = secondeImage / framerateTmp;

        // Image :
        this.image = secondeImage % framerateTmp;
    }

    /**
     * Modifie si le timecode est drop-frame ou non.
     *
     * @param dropFrame La valeur du drop-frame.
     */
    public void setDropFrame(boolean dropFrame) {
        this.dropFrame = dropFrame;
    }

    /**
     * Modifie le framerate.<br>
     * Pour changer de timecode (existant vers un autre), utiliser
     * {@link #changeFramerate(double)}.
     *
     * @param framerate Le framerate.
     */
    public void setFramerate(double framerate) {
        this.framerate = framerate;
    }

    /**
     * Définit le timecode de début.
     *
     * @param timecodeDebut Nombre d'images.
     */
    public void setStartTimecode(String timecodeDebut) {
        this.timecodeDebut = timecodeDebut;
    }

    /**
     * Converti un timecode continu en un timecode en bobine.
     *
     * @param timecodeContinu Le timecode en continu.
     * @param listeBobine Liste des bobines.
     * @return Le timecode en bobine, sinon {@code null}.
     */
    public static Timecode timecodeContinuToReel(@NotNull Timecode timecodeContinu, @NotNull List<Part> listeBobine) {
        int tcImage = timecodeContinu.toImage(true);

        for (Part bobine : listeBobine) {
            if (tcImage < bobine.getDureeImage()) {
                Timecode timecodeBobine = new Timecode(tcImage + bobine.getStartTimecodeImage(), timecodeContinu.framerate);
                timecodeBobine.setStartTimecode(bobine.getStartTimecode().toString());
                return timecodeBobine;
            } else {
                tcImage -= bobine.getDureeImage();
            }
        }

        // Hors scope TC.
        return null;
    }

    /**
     * Converti un timecode en bobine en continu.
     *
     * @param timecodeBobine Le timecode en bobine.
     * @param listeBobine La liste doit être dans l'ordre (bobine 1, bobine 2,
     * etc).
     * @return Le timecode en continu.
     */
    public static Timecode timecodeReelToContinu(@NotNull Timecode timecodeBobine, @NotNull List<Part> listeBobine) {
        return timecodeReelToContinu(timecodeBobine, listeBobine, null);
    }

    /**
     * Converti un timecode en bobine en continu.
     *
     * @param timecodeBobine Le timecode en bobine.
     * @param listeBobine La liste doit être dans l'ordre (bobine 1, bobine 2,
     * etc).
     * @param startTimecode Le timecode début en continu, peut être
     * {@code null}.
     * @return Le timecode en continu.
     */
    @Null
    public static Timecode timecodeReelToContinu(@NotNull Timecode timecodeBobine, @NotNull List<Part> listeBobine, @Null String startTimecode) {
        for (int i = 0; i < listeBobine.size(); i++) {
            // Si les starts TC sont les mêmes, c'est cette bobine :
            if (listeBobine.get(i).getStartTimecode().toString().equals(timecodeBobine.timecodeDebut)) {
                int nombreImage = timecodeBobine.toImage(true);

                // On ne traite pas l'ajout du TC de la bobine actuel.
                // Et on doit ajouter la bobine 1.
                for (i--; i >= 0; i--) {
                    nombreImage += listeBobine.get(i).getDureeImage();
                }

                Timecode timecodeContinu = new Timecode(nombreImage + new Timecode(startTimecode, timecodeBobine.framerate).toImage(), timecodeBobine.framerate);

                // Si c'est null, on ne fait rien.
                if (startTimecode != null) {
                    timecodeContinu.setStartTimecode(startTimecode);
                }

                return timecodeContinu;
            }
        }

        // Si on ne trouve pas, on retourne null.
        return null;
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
     * @param imageUtile Si {@code true} alors le nombre d'images en tenant
     * compte que des images utiles.<br>
     * Doit définir le timecode début via {@link #setStartTimecode(String)}.
     * @return Le nombre d'images que représente le timecode.
     */
    public int toImage(boolean imageUtile) {
        if (this.doitEtreCalcule) {
            this.nombreImageToInt();
            this.doitEtreCalcule = false;
        }

        if (this.isNull.isBlank()) {
            return (this.heure * 60 * 60 * this.getFramerateCalcule()) + (this.minute * 60 * this.getFramerateCalcule()) + (this.seconde * this.getFramerateCalcule()) + (this.image)
                    - ((imageUtile) ? new Timecode(this.timecodeDebut, this.framerate).toImage() : 0) - (this.dropFrame ? compenserDropFrame() : 0);
        } else {
            return -1;
        }
    }

    /**
     * Retourne le timecode en {@link String} sous la représentation SMPTE.
     *
     * @return Le timecode en {@link String}.
     */
    @NotNull
    @NotBlank
    @Override
    public String toString() {
        if (this.doitEtreCalcule) {
            this.nombreImageToInt();
            this.doitEtreCalcule = false;
        }

        if (this.isNull.isBlank()) {
            if (this.dropFrame) {
                return this.dropFrame();
            } else {
                return digit(this.heure) + ":" + digit(this.minute) + ":" + digit(this.seconde) + ":" + digit(this.image);
            }
        } else {
            return "-1";
        }
    }

    /**
     * Retourne {@code true} si le timecode est valide.
     *
     * @param tc Le timecode.
     * @return {@code true} si le timecode est valide.
     */
    public static boolean validation(@NotNull String tc) {
        return validation(tc, Framerate.F30);
    }

    /**
     * Retourne {@code true} si le timecode est valide et selon son framerate.
     *
     * @param tc Le timecode.
     * @param framerate Le framerate.
     * @return {@code true} si le timecode est valide.
     */
    public static boolean validation(@NotNull String tc, @NotNull Framerate framerate) {
        try {
            boolean ok = true;

            tc = tc.replace(";", ":");

            String[] splitTc = tc.split(":");

            if (splitTc.length != 4) {
                ok = false;
            }

            for (int i = 0; i < splitTc.length; i++) {
                if (splitTc[i].length() != 2) {
                    ok = false;
                }

                int splitTcNb = Integer.parseInt(splitTc[i]);

                if (splitTcNb < 0) {
                    ok = false;
                }

                switch (i) {
                    case 0 -> {
                        if (splitTcNb > 24) {
                            ok = false;
                        }
                    }

                    case 1 -> {
                        if (splitTcNb >= 60) {
                            ok = false;
                        }
                    }

                    case 2 -> {
                        if (splitTcNb >= 60) {
                            ok = false;
                        }
                    }

                    case 3 -> {
                        // On ne gère pas au dessus du 30 image seconde.
                        if (splitTcNb >= framerate.value) {
                            ok = false;
                        }
                    }
                }
            }

            return ok;
        } // Si une conversion de nombre n'a pas fonctionné c'est que le TC n'est pas conforme.
        catch (NumberFormatException exception) {
            return false;
        }
    }
}
