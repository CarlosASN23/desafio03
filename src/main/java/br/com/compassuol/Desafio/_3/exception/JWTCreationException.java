package br.com.compassuol.Desafio._3.exception;

public class JWTCreationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JWTCreationException(String message) {
        super(message);
    }

    public JWTCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
