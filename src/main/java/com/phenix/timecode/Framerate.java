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
    F23976(23976D, false),
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
    F2997(2997D, true);

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
     * Retourne le framerate.
     *
     * @return Le framerate.
     */
    public double getValeur() {
        return this.framerate;
    }
}
