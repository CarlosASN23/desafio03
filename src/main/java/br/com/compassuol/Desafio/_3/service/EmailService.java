package br.com.compassuol.Desafio._3.service;

import br.com.compassuol.Desafio._3.exception.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String enviarEmailTexto(String destinatario, String assunto, String mensagem){
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(remetente);
            simpleMailMessage.setTo(destinatario);
            simpleMailMessage.setSubject(assunto);
            simpleMailMessage.setText(mensagem);
            javaMailSender.send(simpleMailMessage);

            return "Email enviado com sucesso";
        }catch (br.com.compassuol.Desafio._3.exception.MailSendException e){
            throw new MailSendException("Erro ao tentar enviar o email " + e.getLocalizedMessage());
        }catch (UsernameNotFoundException e){
            throw new UsernameNotFoundException("Não foi possivel encontrar o email " + destinatario);
        }
    }
}
