package com.phenix.timecode.exceptions;

/**
 * Exception spécifique au projet que toutes les exceptions du projet
 * hérite.<br>
 * Cette exception permet de ne pas être obligatoirement être gérée via des
 * "{@code try/catch}".
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class TimecodeRuntimeException extends RuntimeException {

    /**
     * Construit {@code TimecodeRuntimeException} avec un message d'erreur.
     *
     * @param message Le message.
     */
    public TimecodeRuntimeException(String message) {
        super(message);
    }
}
