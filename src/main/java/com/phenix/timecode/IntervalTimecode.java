package com.phenix.timecode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Interval de timecode.
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public final class IntervalTimecode {

    /**
     * Timecode in.
     */
    @NotNull
    private Timecode tcIn;

    /**
     * Timecode out.
     */
    @NotNull
    private Timecode tcOut;

    /**
     * Un interval.
     *
     * @param tcIn Timecode in.
     * @param tcOut Timecode out.
     * @param framerate Le framerate des deux timecodes.
     */
    public IntervalTimecode(@NotNull @NotBlank String tcIn, @NotNull @NotBlank String tcOut, @NotNull Framerate framerate) {
        this.tcIn = new Timecode(tcIn, framerate);
        this.tcOut = new Timecode(tcOut, framerate);
    }

    /**
     * Un interval.
     *
     * @param tcIn Timecode in.
     * @param tcOut Timecode out.
     */
    public IntervalTimecode(@NotNull Timecode tcIn, @NotNull Timecode tcOut) {
        this.tcIn = tcIn;
        this.tcOut = tcOut;
    }

    /**
     * Retourne {@code true} si le timecode est dans l'interval.
     *
     * @param tc Le timecode à vérifier.
     * @return {@code true} si le TC est dans l'interval.
     */
    public boolean dedans(@NotNull @NotBlank String tc) {
        return this.dedans(new Timecode(tc, this.tcIn.getFramerate()));
    }

    /**
     * Retourne {@code true} si le timecode est dans l'interval.
     *
     * @param tc Le timecode à vérifier.
     * @return {@code true} si le TC est dans l'interval.
     */
    public boolean dedans(@NotNull @NotBlank Timecode tc) {
        return tc.entre(this.tcIn, this.tcOut);
    }

    /**
     * Retourne le timecode in.
     *
     * @return Le timecode in.
     */
    @NotNull
    public Timecode getTimecodeIn() {
        return this.tcIn;
    }

    /**
     * Retourne le timecode out.
     *
     * @return Le timecode out.
     */
    @NotNull
    public Timecode getTimecodeOut() {
        return this.tcOut;
    }

    /**
     * Définit le timecode in.
     *
     * @param tcIn Timecode in.
     */
    public void setTimecodeIn(@NotNull Timecode tcIn) {
        this.tcIn = tcIn;
    }

    /**
     * Définit le timecode out.
     *
     * @param tcOut Timecode out.
     */
    public void setTimecodeOut(@NotNull Timecode tcOut) {
        this.tcOut = tcOut;
    }
}
