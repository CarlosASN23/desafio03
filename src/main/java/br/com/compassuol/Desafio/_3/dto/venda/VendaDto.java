package br.com.compassuol.Desafio._3.dto.venda;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.annotations.CurrentTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

public record VendaDto(
                        @NotNull
                        @CurrentTimestamp
                        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ssX")
                        LocalDateTime dataVenda,
                        @NotNull
                        @Positive
                        Integer statusVenda,
                        @NotNull
                        @PositiveOrZero
                        Double valorVenda){

}
