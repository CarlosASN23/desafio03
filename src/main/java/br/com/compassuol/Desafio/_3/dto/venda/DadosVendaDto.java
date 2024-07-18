package br.com.compassuol.Desafio._3.dto.venda;

import br.com.compassuol.Desafio._3.model.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.annotations.CurrentTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

public record DadosVendaDto(
                            @NotNull
                            Long id,
                            @NotNull
                            @CurrentTimestamp
                            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
                            LocalDateTime dataVenda,
                            br.com.compassuol.Desafio._3.model.enums.StatusVenda statusVenda,
                            @PositiveOrZero
                            Double valorVenda,
                            @NotNull
                            Long idUsuario) {
}
