package br.com.compassuol.Desafio._3.service;

import br.com.compassuol.Desafio._3.dto.usuario.PasswordTokenPublicData;
import br.com.compassuol.Desafio._3.exception.JwtException;
import br.com.compassuol.Desafio._3.exception.KeyVerificationException;
import br.com.compassuol.Desafio._3.exception.TokenExpiredException;
import br.com.compassuol.Desafio._3.exception.TokenGenerationException;
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
        try {
            KeyBasedPersistenceTokenService tokenService = getInstanceFor(usuario);
            Token token = tokenService.allocateToken(usuario.getEmail());
            return token.getKey();
        }catch (TokenGenerationException e){
            throw new TokenGenerationException("Não foi possivel gerar o token");
        }catch (JwtException e){
            throw new JwtException("Erro na configuração da biblioteca JWT");
        }
    }

    // Alterar a senha do usuario
    public void changePassword(String newPassword, String rawToken) throws Exception {
        try{
            // chamando a função readPublicData(rawToken), que decodifica o token fornecido (rawToken)
            // e extrai informações relevantes (como o email e o timestamp).
            PasswordTokenPublicData publicData = readPublicData(rawToken);

            if(isExpired(publicData)){
                throw new RuntimeException("Token expirado");
            }
            // Buscando informação do email no banco de dados
            Usuario usuario = (Usuario) usuarioRepository.findByEmail(publicData.getEmail());

            // Instanciando KeyBasedPersistenceTokenService
            KeyBasedPersistenceTokenService tokenService = this.getInstanceFor(usuario);

            // Verificação do token
            tokenService.verifyToken(rawToken);

            // Se o token não estiver expirado, o código atualiza a senha do usuário com a nova senha fornecida
            usuario.setSenha(this.passwordEncoder.encode(newPassword));
            usuarioRepository.save(usuario);

        }catch (TokenExpiredException e){
            throw new TokenExpiredException ("Token expirado");
        }
    }

    // Verificar se o token esta expirado
    private boolean isExpired(PasswordTokenPublicData publicData) {
        try {
            Instant createdAt = new Date(publicData.getCreatedAtTimestamp()).toInstant();
            Instant now = new Date().toInstant();
            return createdAt.plus(Duration.ofMinutes(5)).isBefore(now);
        }catch (KeyVerificationException e){
            throw new KeyVerificationException ("A chave token já foi utilizada");
        }
    }

    // método para criar e configurar uma instância com base nas informações do usuário,
    // como a senha e o valor inteiro do servidor.
    private KeyBasedPersistenceTokenService getInstanceFor(Usuario usuario) throws Exception {

        // Instanciando KeyBasedPersistenceTokenService
        KeyBasedPersistenceTokenService  tokenService = new KeyBasedPersistenceTokenService();

        // Define o segredo do servidor (ou “serverSecret”) com base na senha do usuário fornecida.
        // Esse segredo é usado para gerar as chaves dos tokens.
        tokenService.setServerSecret(usuario.getSenha());

        // Define um valor inteiro do servidor (ou “serverInteger”).
        // Esse valor é usado para calcular o segredo do servidor em cada milissegundo.
        tokenService.setServerInteger(16);

        //Configura um gerador de números pseudoaleatórios seguro para uso interno.
        tokenService.setSecureRandom(new SecureRandomFactoryBean().getObject());
        return tokenService;
    }

    // Método para decodificar um token de senha e extrair informações para criar um objeto
    private PasswordTokenPublicData readPublicData(String rawToken){

        //decodificando o token base64 fornecido como entrada
        String rawTokenDecoded = new String(Base64.getDecoder().decode(rawToken));

        // divide essa string usando o caractere “:” como separador e armazena as partes resultantes
        // em um array chamado tokenParts
        String[] tokenParts = rawTokenDecoded.split(":");

        Long timeStamp = Long.parseLong(tokenParts[0]);
        String email = tokenParts[2];

        //o método cria um novo objeto PasswordTokenPublicData com o email e o timestamp extraídos.
        return new PasswordTokenPublicData(email, timeStamp);
    }

}

