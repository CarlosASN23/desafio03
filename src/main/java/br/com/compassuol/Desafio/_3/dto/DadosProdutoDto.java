package br.com.compassuol.Desafio._3.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

public record DadosProdutoDto (
                                @NotNull
                                Long id,
                                String nome,
                                @PositiveOrZero
                                Double preco,
                                Boolean ativo,
                                @PositiveOrZero
                                Integer estoque) implements Serializable{
    private static final long serialVersion = 1L;
}
