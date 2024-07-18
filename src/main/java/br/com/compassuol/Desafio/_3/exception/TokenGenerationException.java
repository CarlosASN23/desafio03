package br.com.compassuol.Desafio._3.exception;

public class TokenGenerationException extends Exception {
    private static final long serialVersionUID = 1L;

    public TokenGenerationException(String message){
        super(message);
    }

    public TokenGenerationException(String message, Throwable cause){
        super(message, cause);
    }
}
