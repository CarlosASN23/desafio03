package br.com.compassuol.Desafio._3.controller;

import br.com.compassuol.Desafio._3.dto.LoginResponseDto;
import br.com.compassuol.Desafio._3.dto.RegistroUserDto;
import br.com.compassuol.Desafio._3.dto.UsuarioDto;
import br.com.compassuol.Desafio._3.model.Usuario;
import br.com.compassuol.Desafio._3.repository.UsuarioRepository;
import br.com.compassuol.Desafio._3.security.TokenService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid UsuarioDto usuario){
        var usernamePassword = new UsernamePasswordAuthenticationToken(usuario.email(),usuario.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/registrar")
    @Transactional
    public ResponseEntity<String> registrar(@RequestBody @Valid RegistroUserDto registroUserDto) {
        if (this.usuarioRepository.findByEmail(registroUserDto.email()) != null) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado.");
        }

        String encryptedPass = new BCryptPasswordEncoder().encode(registroUserDto.senha());
        Usuario usuario = new Usuario(registroUserDto.email(),encryptedPass, registroUserDto.role());

        this.usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }

}