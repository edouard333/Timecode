package com.phenix.timecode.exception;

import jakarta.validation.constraints.NotNull;

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
     * Construit une {@link TimecodeRuntimeException} avec un message.
     *
     * @param message Le message.
     */
    public TimecodeRuntimeException(String message) {
        super(message);
    }

    /**
     * Construit une {@link TimecodeRuntimeException} avec un message et une
     * cause.
     *
     * @param message Le message.
     * @param cause La cause.
     */
    public TimecodeRuntimeException(String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}
