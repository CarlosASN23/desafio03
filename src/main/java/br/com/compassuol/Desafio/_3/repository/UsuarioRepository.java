package br.com.compassuol.Desafio._3.repository;

import br.com.compassuol.Desafio._3.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    UserDetails findByEmail(String username);
}
