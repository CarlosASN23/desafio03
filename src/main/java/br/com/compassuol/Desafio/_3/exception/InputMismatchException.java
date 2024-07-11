package br.com.compassuol.Desafio._3.exception;

public class InputMismatchException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InputMismatchException(String message) {
        super(message);
    }

    public InputMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
