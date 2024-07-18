package br.com.compassuol.Desafio._3.exception;


public class DuplicatedObjectException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public DuplicatedObjectException(String message){
        super(message);
    }

    public DuplicatedObjectException(String message, Throwable cause){
        super(message, cause);
    }
}