package br.com.compassuol.Desafio._3.dto;

import br.com.compassuol.Desafio._3.model.Produto;
import br.com.compassuol.Desafio._3.model.Venda;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.annotations.CurrentTimestamp;

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
                                @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ssX")
                                ZonedDateTime dataItemPedido) {
}
