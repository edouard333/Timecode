package com.phenix.timecode;

/**
 * Interval de timecode.
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public final class IntervalTimecode {

    /**
     * Timecode in.
     */
    private Timecode tc_in;

    /**
     * Timecode out.
     */
    private Timecode tc_out;

    /**
     *
     * @param tc_in Timecode in.
     * @param tc_out Timecode out.
     * @param framerate Le framerate des deux timecodes.
     */
    public IntervalTimecode(String tc_in, String tc_out, Framerate framerate) {
        this.tc_in = new Timecode(tc_in, framerate);
        this.tc_out = new Timecode(tc_out, framerate);
    }

    /**
     *
     * @param tc_in Timecode in.
     * @param tc_out Timecode out.
     */
    public IntervalTimecode(Timecode tc_in, Timecode tc_out) {
        this.tc_in = tc_in;
        this.tc_out = tc_out;
    }

    /**
     *
     * @param tc
     * @return
     */
    public boolean dedans(String tc) {
        return this.dedans(new Timecode(tc, tc_in.getFramerate()));
    }

    /**
     *
     * @param tc
     * @return
     */
    public boolean dedans(Timecode tc) {
        return tc.entre(tc_in, tc_out);
    }

    /**
     *
     * @return Le timecode in.
     */
    public Timecode getTimecodeIn() {
        return this.tc_in;
    }

    /**
     *
     * @return Le timecode out.
     */
    public Timecode getTimecodeOut() {
        return this.tc_out;
    }

    /**
     * Définit le timecode in.
     *
     * @param tc_in Timecode in.
     */
    public void setTimecodeIn(Timecode tc_in) {
        this.tc_in = tc_in;
    }

    /**
     * Définit le timecode out.
     *
     * @param tc_out Timecode out.
     */
    public void setTimecodeOut(Timecode tc_out) {
        this.tc_out = tc_out;
    }
}
