package com.phenix.timecode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

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
    public final double value;

    /**
     * Si drop frame ou non.
     */
    public final boolean dropFrame;

    /**
     * Définit un frame rate.
     *
     * @param value Le framerate.
     * @param dropFrame Si on est en dropframe ou non.
     */
    private Framerate(double value, boolean dropFrame) {
        this.value = value;
        this.dropFrame = dropFrame;
    }

    /**
     * Retourne un {@link Framerate} en fonction d'une valeur en {@link String}.
     *
     * @param value La valeur.
     * @return Soite le {@link Framerate} associé sinon {@code null}.
     */
    @Null
    public static Framerate fromValue(@NotNull String value) {
        double valueD = Double.parseDouble(value);

        for (Framerate framerate : values()) {
            if (framerate.value == valueD) {
                return framerate;
            }
        }

        return null;
    }

    /**
     * Retourne un {@link Framerate} en fonction d'une valeur en {@code double}.
     *
     * @param value La valeur.
     * @return Soite le {@link Framerate} associé sinon {@code null}.
     */
    @Null
    public static Framerate fromValue(double value) {
        for (Framerate framerate : values()) {
            if (framerate.value == value) {
                return framerate;
            }
        }

        return null;
    }
}
