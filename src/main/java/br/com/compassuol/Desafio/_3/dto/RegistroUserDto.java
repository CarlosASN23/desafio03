package br.com.compassuol.Desafio._3.dto;

import br.com.compassuol.Desafio._3.model.enums.UserRoles;

public record RegistroUserDto(String email, String senha, UserRoles role) {
}
