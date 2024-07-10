package br.com.compassuol.Desafio._3.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.ZonedDateTime;
import java.util.Date;

public record DadosVendaDto(
                            @NotNull
                            Long id,
                            @NotNull
                            @CurrentTimestamp
                            @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ssX")
                            ZonedDateTime dataVenda,
                            @Positive
                            Integer statusVenda,
                            @PositiveOrZero
                            Double valorVenda) {
}
