package br.com.compassuol.Desafio._3.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosAtualizacaoSenha(@NotBlank String senha, @NotBlank String token) {
}
