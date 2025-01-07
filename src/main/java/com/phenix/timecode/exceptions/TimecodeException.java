package com.phenix.timecode.exceptions;

/**
 * Exception spécifique au projet que toutes les exceptions du projet hérite.
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class TimecodeException extends Exception {

    /**
     * Construit {@code TimecodeException} avec un message d'erreur.
     *
     * @param message Le message.
     */
    public TimecodeException(String message) {
        super(message);
    }
}
