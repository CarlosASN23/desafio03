package br.com.compassuol.Desafio._3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

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
                            Integer estoque) implements Serializable {

    private static final long serialVersionUID = 5620606054205035360L;
}
