package br.com.compassuol.Desafio._3.dto.usuario;

import br.com.compassuol.Desafio._3.model.enums.UserRoles;

public record RegistroUserDto(String email, String senha, UserRoles role) {
}
