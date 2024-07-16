package br.com.compassuol.Desafio._3.dto;

import jakarta.validation.constraints.NotNull;

public record DadosUsuarioDto(
        @NotNull
        Long id,
        @NotNull
        String email) {
}
