package br.com.compassuol.Desafio._3.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(),"Bad Request",e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicatedObjectException.class)
    public ResponseEntity<StandardError> duplicateObject(DuplicatedObjectException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server error",e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InputMismatchException.class)
    public ResponseEntity<StandardError> inputMismatch(InputMismatchException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(),"Bad Requestr",e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<StandardError>InvalidDate(InvalidDateException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoItemInSalesException.class)
    public ResponseEntity<StandardError> NoItemInSales(NoItemInSalesException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(err);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoProdutcAtiveExcetion.class)
    public ResponseEntity<StandardError> NoProdutcAtiveExcetion(NoProdutcAtiveExcetion e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(err);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NullPointerException.class)
    public ResponseEntity<StandardError> NullPointerException(NullPointerException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(err);
    }

}
