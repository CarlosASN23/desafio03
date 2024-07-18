package br.com.compassuol.Desafio._3.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DadosRecuperacaoSenha(@Email @NotNull String email) {
}
