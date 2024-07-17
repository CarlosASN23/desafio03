package br.com.compassuol.Desafio._3.dto.usuario;

import jakarta.validation.constraints.Email;

public record UsuarioDto(@Email String email, String senha) {
}
