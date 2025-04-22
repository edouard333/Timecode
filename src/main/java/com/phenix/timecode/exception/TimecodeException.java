package com.phenix.timecode.exception;

/**
 * Exception de base pour toutes les erreurs survenant dans le projet.<br>
 * <br>
 * Toutes les exceptions spécifiques doivent hériter de cette classe.
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class TimecodeException extends Exception {

    /**
     * Construit une {@link TimecodeException} avec un message.
     *
     * @param message Le message.
     */
    public TimecodeException(String message) {
        super(message);
    }
}
