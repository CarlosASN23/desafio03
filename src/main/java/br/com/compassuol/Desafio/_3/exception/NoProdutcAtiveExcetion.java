package br.com.compassuol.Desafio._3.exception;

public class NoProdutcAtiveExcetion extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public NoProdutcAtiveExcetion(String message){
        super(message);
    }

    public NoProdutcAtiveExcetion(String message, Throwable cause){
        super(message, cause);
    }
}
