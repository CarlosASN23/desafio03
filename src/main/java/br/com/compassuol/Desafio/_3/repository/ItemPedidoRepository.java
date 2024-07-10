package br.com.compassuol.Desafio._3.repository;

import br.com.compassuol.Desafio._3.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido,Long> {

    @Query("SELECT ip FROM ItemPedido ip WHERE ip.venda.idVenda = :idVenda")
    List<ItemPedido> exibirItensPorVendaId(long idVenda);

    List<ItemPedido> findByDataItemPedidoBetween(LocalDateTime semanaInicial, LocalDateTime semanaFinal);

}
