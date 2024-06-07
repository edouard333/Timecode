package com.phenix.timecode;

/**
 * Liste les différents framerates.
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public enum Framerate {

    /**
     * Framerate 23,976is.
     */
    F23976(23.976D, false),
    /**
     * Framerate 24is (cinéma/Blu-ray).
     */
    F24(24D, false),
    /**
     * Framerate 25is (PAL TV).
     */
    F25(25D, false),
    /**
     * Framerate 29,97 (NTSC TV).
     */
    F2997(29.97D, true),
    /**
     * Framerate 29,97 (NTSC TV).
     */
    F2997ND(29.97D, false),
    /**
     * Framerate 30 (NTSC TV).
     */
    F30(30D, false);

    /**
     * Valeur à utiliser.
     */
    private final double framerate;

    /**
     * Si drop frame ou non.
     */
    private final boolean drop_frame;

    /**
     * Définit un frame rate.
     *
     * @param framerate Le framerate.
     * @param drop_frame Si on est en dropframe ou non.
     */
    private Framerate(double framerate, boolean drop_frame) {
        this.framerate = framerate;
        this.drop_frame = drop_frame;
    }

    /**
     *
     * @param value
     * @return
     */
    public static Framerate fromValue(String value) {
        double value_d = Double.parseDouble(value);
        Framerate[] liste_framerate = values();

        Framerate framerate;
        for (int i = 0; i < liste_framerate.length; i++) {
            framerate = liste_framerate[i];
            if (framerate.getValeur() == value_d) {
                return framerate;
            }
        }

        return null;
    }

    /**
     * Retourne le framerate.
     *
     * @return Le framerate.
     */
    public double getValeur() {
        return this.framerate;
    }

    /**
     * Retourne si le framerate est en dropframe ou non.
     *
     * @return Le dropframe.
     */
    public boolean getDropFrame() {
        return this.drop_frame;
    }
}
