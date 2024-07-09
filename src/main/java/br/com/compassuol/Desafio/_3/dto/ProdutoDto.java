package br.com.compassuol.Desafio._3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProdutoDto(
                            @NotBlank
                            String nome,
                            @NotNull
                            @PositiveOrZero
                            Double preco,
                            @NotBlank
                            Boolean ativo,
                            @NotNull
                            @PositiveOrZero
                            Integer estoque){
}
