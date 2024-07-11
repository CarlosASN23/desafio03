package br.com.compassuol.Desafio._3.exception;

public class NullPointerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NullPointerException(String message) {
        super(message);
    }

    public NullPointerException(String message, Throwable cause) {
        super(message, cause);
    }
}
