package br.com.compassuol.Desafio._3.repository;

import br.com.compassuol.Desafio._3.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findAllByAtivoTrue();
    Optional<Produto> findByNomeContainingIgnoreCase(String nome);
}
