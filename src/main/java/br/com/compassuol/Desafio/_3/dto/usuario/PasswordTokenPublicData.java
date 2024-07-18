package br.com.compassuol.Desafio._3.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordTokenPublicData {

    private final String email;
    private final Long createdAtTimestamp;
}
