package br.com.compassuol.Desafio._3.exception;

public class MailSendException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public MailSendException(String message){
        super(message);
    }

    public MailSendException(String message, Throwable cause){
        super(message, cause);
    }
}
