package br.com.compassuol.Desafio._3.service;

import br.com.compassuol.Desafio._3.dto.usuario.PasswordTokenPublicData;
import br.com.compassuol.Desafio._3.model.Usuario;
import br.com.compassuol.Desafio._3.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.SecureRandomFactoryBean;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username);
    }

    // Gerar token transparente sem necessidade de armazenar no banco de dados
    public String generateToken(Usuario usuario) throws Exception {
        KeyBasedPersistenceTokenService tokenService = getInstanceFor(usuario);

        Token token = tokenService.allocateToken(usuario.getEmail());

        return token.getKey();
    }

    // Alterar a senha do usuario
    public void changePassword(String newPassword, String rawToken) throws Exception {
        PasswordTokenPublicData publicData = readPublicData(rawToken);

        if(isExpired(publicData)){
            throw new RuntimeException("Token expirado");
        }

        Usuario usuario = (Usuario) usuarioRepository.findByEmail(publicData.getEmail());

        KeyBasedPersistenceTokenService tokenService = this.getInstanceFor(usuario);
        tokenService.verifyToken(rawToken);

        usuario.setSenha(this.passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
    }

    private boolean isExpired(PasswordTokenPublicData publicData) {
        Instant createdAt = new Date(publicData.getCreatedAtTimestamp()).toInstant();
        Instant now = new Date().toInstant();
        return createdAt.plus(Duration.ofMinutes(5)).isBefore(now);
    }

    private KeyBasedPersistenceTokenService getInstanceFor(Usuario usuario) throws Exception {
        KeyBasedPersistenceTokenService  tokenService = new KeyBasedPersistenceTokenService();

        tokenService.setServerSecret(usuario.getSenha());
        tokenService.setServerInteger(16);
        tokenService.setSecureRandom(new SecureRandomFactoryBean().getObject());
        return tokenService;
    }

    private PasswordTokenPublicData readPublicData(String rawToken){
        String rawTokenDecoded = new String(Base64.getDecoder().decode(rawToken));
        String[] tokenParts = rawTokenDecoded.split(":");
        Long timeStamp = Long.parseLong(tokenParts[0]);
        String email = tokenParts[2];
        return new PasswordTokenPublicData(email, timeStamp);
    }

}

