package br.com.compassuol.Desafio._3.dto.venda;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DadosUsuarioDto(
        @NotNull
        Long id,
        @NotNull
        @Email
        String email) {
}
