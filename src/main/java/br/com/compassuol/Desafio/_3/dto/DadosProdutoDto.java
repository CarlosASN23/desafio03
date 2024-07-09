package br.com.compassuol.Desafio._3.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record DadosProdutoDto(
                                @NotNull
                                Long id,
                                String nome,
                                @PositiveOrZero
                                Double preco,
                                Boolean ativo,
                                @PositiveOrZero
                                Integer estoque) {
}
