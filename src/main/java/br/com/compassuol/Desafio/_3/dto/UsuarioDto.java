package br.com.compassuol.Desafio._3.dto;

import jakarta.validation.constraints.Email;

public record UsuarioDto(@Email String email, String senha) {
}
