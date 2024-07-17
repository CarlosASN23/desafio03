package br.com.compassuol.Desafio._3.exception;

public class KeyVerificationException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public KeyVerificationException(String message){
        super(message);
    }

    public KeyVerificationException(String message, Throwable cause){
        super(message, cause);
    }
}
