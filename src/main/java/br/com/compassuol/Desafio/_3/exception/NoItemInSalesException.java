package br.com.compassuol.Desafio._3.exception;

public class NoItemInSalesException extends RuntimeException {
        private static final long serialVersionUID = 1L;

    public NoItemInSalesException(String message){
            super(message);
        }

    public NoItemInSalesException(String message, Throwable cause){
            super(message, cause);

    }
}
