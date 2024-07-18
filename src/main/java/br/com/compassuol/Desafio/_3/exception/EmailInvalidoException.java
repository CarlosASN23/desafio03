package br.com.compassuol.Desafio._3.exception;


public class EmailInvalidoException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public EmailInvalidoException(String message){
        super(message);
    }

    public EmailInvalidoException(String message, Throwable cause){
        super(message, cause);
    }
}