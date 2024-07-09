package br.com.compassuol.Desafio._3.model;

import br.com.compassuol.Desafio._3.model.enums.StatusVenda;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "vendas")
public class Venda implements Serializable {

    private static final long serialVersion = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @CurrentTimestamp
    @Column(name = "Data_Venda",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dataCriacao;

    @Column(name = "Status_venda",nullable = false)
    private Integer statusVenda;

    @Column(name = "Valor_venda",nullable = false)
    private Double valorVenda;

    public StatusVenda getStatusVenda() {
        return StatusVenda.toEnum(statusVenda);
    }
    public void setStatusVenda(StatusVenda statusVenda){
        this.statusVenda = statusVenda.getCod();
    }

    @Override
    public String toString() {
        return "Venda{" +
                "Id = " + id +
                ", Data Criacao = " + dataCriacao +
                ", Status da Venda = " + statusVenda +
                ", Valor Venda = R$" + valorVenda;
    }
}
