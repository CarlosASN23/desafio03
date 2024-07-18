package br.com.compassuol.Desafio._3.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    // ObjectNotFoundException - Dados não encontrado no banco de dados
    @org.springframework.web.bind.annotation.ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(),"Bad Request",e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    // DuplicatedObjectException - Dados duplicados no banco de dados
    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicatedObjectException.class)
    public ResponseEntity<StandardError> duplicateObject(DuplicatedObjectException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server error",e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    // InputMismatchException - Entrada de dados inválidas
    @org.springframework.web.bind.annotation.ExceptionHandler(InputMismatchException.class)
    public ResponseEntity<StandardError> inputMismatch(InputMismatchException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(),"Bad Request",e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    // InvalidDateException - Entrada de datas em formato inválido
    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<StandardError>InvalidDate(InvalidDateException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    // NoItemInSalesException - Tentativa de realizar vendas sem passagem do item
    @org.springframework.web.bind.annotation.ExceptionHandler(NoItemInSalesException.class)
    public ResponseEntity<StandardError> NoItemInSales(NoItemInSalesException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(err);
    }

    //NoProdutcAtiveExcetion - Produto deve estar ativo para ser adicionado a uma venda
    @org.springframework.web.bind.annotation.ExceptionHandler(NoProdutcAtiveExcetion.class)
    public ResponseEntity<StandardError> NoProdutcAtiveExcetion(NoProdutcAtiveExcetion e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(err);
    }

    // NullPointerException - Dados com valor nulo
    @org.springframework.web.bind.annotation.ExceptionHandler(NullPointerException.class)
    public ResponseEntity<StandardError> NullPointerException(NullPointerException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(err);
    }

    // MailSendException - Exceção ao tentar enviar o email de recuperação de senha
    @org.springframework.web.bind.annotation.ExceptionHandler(MailSendException.class)
    public ResponseEntity<StandardError> MailSendException(MailSendException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(),"Bad Request", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(err);
    }

    // UsernameNotFoundException - E-mail de usuario não encontrado no banco de dados
    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<StandardError> UsernameNotFoundException(UsernameNotFoundException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(),"Bad Request", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(err);
    }

    // TokenExpiredException - Tratamento para a expiração do tokens
    @org.springframework.web.bind.annotation.ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<StandardError> TokenExpiredException(TokenExpiredException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.FORBIDDEN.value(),"Forbidden", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(err);
    }

    // TokenGenerationException - Tratamento para a falha na geração do token
    @org.springframework.web.bind.annotation.ExceptionHandler(TokenGenerationException.class)
    public ResponseEntity<StandardError> TokenGenerationException(TokenGenerationException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.FORBIDDEN.value(),"Forbidden", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(err);
    }

    // JwtException - Tratamento de erro para o caso de erro ao acessar a biblioteca JWT
    @org.springframework.web.bind.annotation.ExceptionHandler(JwtException.class)
    public ResponseEntity<StandardError> JwtException (JwtException  e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.FORBIDDEN.value(),"Forbidden", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(err);
    }

    // KeyVerificationException - Tratamento de erro para o caso de token de recuperação de senha já utilizado
    @org.springframework.web.bind.annotation.ExceptionHandler(KeyVerificationException.class)
    public ResponseEntity<StandardError> KeyVerificationException (KeyVerificationException  e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.FORBIDDEN.value(),"Forbidden", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(err);
    }

    //EmailInvalidoException - Passagem de email inválido
    @org.springframework.web.bind.annotation.ExceptionHandler(EmailInvalidoException.class)
    public ResponseEntity<StandardError> EmailInvalidoException (EmailInvalidoException  e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.FORBIDDEN.value(),"Forbidden", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(err);
    }

}
