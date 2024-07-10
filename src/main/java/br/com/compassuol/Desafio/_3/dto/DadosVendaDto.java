package br.com.compassuol.Desafio._3.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public record DadosVendaDto(
                            @NotNull
                            Long id,
                            @NotNull
                            @CurrentTimestamp
                            @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
                            LocalDateTime dataVenda,
                            br.com.compassuol.Desafio._3.model.enums.StatusVenda statusVenda,
                            @PositiveOrZero
                            Double valorVenda) {
}
