package br.com.compassuol.Desafio._3.repository;

import br.com.compassuol.Desafio._3.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendaRepository extends JpaRepository<Venda,Long> {

}
