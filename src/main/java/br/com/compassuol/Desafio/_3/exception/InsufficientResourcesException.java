package br.com.compassuol.Desafio._3.exception;

public class InsufficientResourcesException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public InsufficientResourcesException(String message){
        super(message);
    }

    public InsufficientResourcesException(String message, Throwable cause){
        super(message, cause);
    }
}
