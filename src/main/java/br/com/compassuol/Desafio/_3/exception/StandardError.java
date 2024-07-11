package br.com.compassuol.Desafio._3.exception;

import java.io.Serializable;

public class StandardError implements Serializable {

    private static final long serialVersionUID =1L;

    private Integer status;
    private String message;
    private String field;

    public StandardError(Integer status, String message, String field) {
        this.status = status;
        this.message = message;
        this.field = field;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}