package br.com.compassuol.Desafio._3.dto;

import br.com.compassuol.Desafio._3.model.Produto;
import br.com.compassuol.Desafio._3.model.Venda;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.annotations.CurrentTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public record DadosItemPedidoDto(

                                @NotNull
                                Long idItemPedido,
                                @NotNull
                                Venda venda,
                                @NotNull
                                Produto produto,
                                @NotNull
                                Double precoItem,
                                @NotNull
                                @Positive
                                Integer quantidadeItem,
                                @CurrentTimestamp
                                @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
                                LocalDateTime dataItemPedido) implements Serializable {

    private static final long serialVersion = 1L;
}
