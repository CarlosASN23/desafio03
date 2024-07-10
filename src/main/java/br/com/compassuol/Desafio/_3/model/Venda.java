package br.com.compassuol.Desafio._3.model;

import br.com.compassuol.Desafio._3.dto.DadosVendaDto;
import br.com.compassuol.Desafio._3.model.enums.StatusVenda;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "vendas")
public class Venda implements Serializable {

    private static final long serialVersion = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long idVenda;

    @CurrentTimestamp
    @Column(name = "Data_Venda",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime dataCriacao;

    @Column(name = "Status_venda",nullable = false)
    private Integer statusVenda;

    @Column(name = "Valor_venda",nullable = false)
    private Double valorVenda;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "venda", cascade = CascadeType.ALL)
    @Valid
    private List<ItemPedido> itens = new ArrayList<>();

    public StatusVenda getStatusVenda() {
        return StatusVenda.toEnum(statusVenda);
    }
    public void setStatusVenda(StatusVenda statusVenda){
        this.statusVenda = statusVenda.getCod();
    }

    @Override
    public String toString() {
        return "Venda{" +
                "Id = " + idVenda +
                ", Data Criacao = " + dataCriacao +
                ", Status da Venda = " + statusVenda +
                ", Valor Venda = R$" + valorVenda;
    }

    public DadosVendaDto atualizarInformacoes(DadosVendaDto dados) {

        if(dados.statusVenda() != null) {
            this.statusVenda = dados.statusVenda().getCod();
        }
        if(dados.valorVenda() != null){
            this.valorVenda = dados.valorVenda();
        }
        return dados;
    }
}
