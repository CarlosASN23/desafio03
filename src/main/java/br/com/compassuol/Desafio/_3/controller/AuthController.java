package br.com.compassuol.Desafio._3.controller;

import br.com.compassuol.Desafio._3.dto.usuario.*;
import br.com.compassuol.Desafio._3.exception.*;
import br.com.compassuol.Desafio._3.model.Usuario;
import br.com.compassuol.Desafio._3.repository.UsuarioRepository;
import br.com.compassuol.Desafio._3.security.TokenService;
import br.com.compassuol.Desafio._3.service.EmailService;
import br.com.compassuol.Desafio._3.service.UsuarioService;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private EmailService emailService;

    @PostMapping
    @Transactional
    public ResponseEntity<?> login(@RequestBody @Valid UsuarioDto usuario) throws TokenGenerationException {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(usuario.email(), usuario.senha());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((Usuario) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDto(token));

        }catch (InputMismatchException e){
            throw new InputMismatchException("Dados informados inválidos");

        }catch (JwtException e){
            throw new JwtException("Não foi possivel acessar a biblioteca JWT");
        }
    }

    @PostMapping("/registrar")
    @Transactional
    public ResponseEntity<String> registrar(@RequestBody @Valid RegistroUserDto registroUserDto) {
        try {
            if (this.usuarioRepository.findByEmail(registroUserDto.email()) != null) {
                return ResponseEntity.badRequest().body("E-mail já cadastrado.");
            }
            String encryptedPass = new BCryptPasswordEncoder().encode(registroUserDto.senha());
            Usuario usuario = new Usuario(registroUserDto.email(), encryptedPass, registroUserDto.role());

            this.usuarioRepository.save(usuario);
            return ResponseEntity.ok("Usuário registrado com sucesso!");

        }catch (InputMismatchException e){
            throw new InputMismatchException("Entrada de dados inválidas");

        }catch (EmailInvalidoException e){
            throw new EmailInvalidoException("Entre com um email válido");
        }
    }

    @PostMapping("/esquecer-senha")
    @Transactional
    public ResponseEntity<String> esquecerSenha (@RequestBody @Valid DadosRecuperacaoSenha dados) throws Exception {
        try{
            UserDetails usuario = usuarioRepository.findByEmail(dados.email());

            if(!usuario.getUsername().isEmpty()){
                UserDetails user = usuario;
                String token = usuarioService.generateToken((Usuario) user);

                String assunto = "Redefinição de senha";
                String text = "Seu token para redefinição de senha é: " + token;
                emailService.enviarEmailTexto(((Usuario) user).getEmail(),assunto, text);

                return ResponseEntity.ok("E-mail enviado com sucesso");
            }
        }catch (UsernameNotFoundException e){

            throw new UsernameNotFoundException("Não foi possivel encontrar o email:" + dados.email());

        }catch (TokenGenerationException e){

            throw new TokenGenerationException("Não foi possivel gerar o token");
        }
        return null;
    }

    @PostMapping("/mudar-senha")
    @Transactional
    public ResponseEntity<String> mudarSenha(@RequestBody @Valid DadosAtualizacaoSenha entrada) throws Exception {
        try{
            usuarioService.changePassword(entrada.senha(),entrada.token());

            return ResponseEntity.ok("Senha alterada com sucesso");

        } catch (InputMismatchException e) {
            throw new InputMismatchException("Dados de entrada Inválida");

        }catch (TokenExpiredException e){
            throw new br.com.compassuol.Desafio._3.exception.TokenExpiredException("Token expirado");

        }catch (KeyVerificationException e){
            throw new KeyVerificationException("O token já foi utilizado");
        }
    }
}