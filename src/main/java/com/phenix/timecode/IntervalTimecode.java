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
    private Timecode tc_in;

    /**
     * Timecode out.
     */
    @NotNull
    private Timecode tc_out;

    /**
     *
     * @param tc_in Timecode in.
     * @param tc_out Timecode out.
     * @param framerate Le framerate des deux timecodes.
     */
    public IntervalTimecode(@NotNull @NotBlank String tc_in, @NotNull @NotBlank String tc_out, Framerate framerate) {
        this.tc_in = new Timecode(tc_in, framerate);
        this.tc_out = new Timecode(tc_out, framerate);
    }

    /**
     *
     * @param tc_in Timecode in.
     * @param tc_out Timecode out.
     */
    public IntervalTimecode(@NotNull Timecode tc_in, @NotNull Timecode tc_out) {
        this.tc_in = tc_in;
        this.tc_out = tc_out;
    }

    /**
     *
     * @param tc
     * @return
     */
    public boolean dedans(@NotNull @NotBlank String tc) {
        return this.dedans(new Timecode(tc, this.tc_in.getFramerate()));
    }

    /**
     *
     * @param tc
     * @return
     */
    public boolean dedans(@NotNull @NotBlank Timecode tc) {
        return tc.entre(this.tc_in, this.tc_out);
    }

    /**
     *
     * @return Le timecode in.
     */
    @NotNull
    public Timecode getTimecodeIn() {
        return this.tc_in;
    }

    /**
     *
     * @return Le timecode out.
     */
    @NotNull
    public Timecode getTimecodeOut() {
        return this.tc_out;
    }

    /**
     * Définit le timecode in.
     *
     * @param tc_in Timecode in.
     */
    public void setTimecodeIn(@NotNull Timecode tc_in) {
        this.tc_in = tc_in;
    }

    /**
     * Définit le timecode out.
     *
     * @param tc_out Timecode out.
     */
    public void setTimecodeOut(@NotNull Timecode tc_out) {
        this.tc_out = tc_out;
    }
}
