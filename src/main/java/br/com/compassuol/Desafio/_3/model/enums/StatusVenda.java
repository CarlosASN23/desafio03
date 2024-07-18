package br.com.compassuol.Desafio._3.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusVenda {

    PENDENTE(1, "PENDENTE"),
    EFETIVADA(2,"EFETIVADA"),
    CANCELADA(3,"CANCELADA");

    private Integer cod;
    private String descricao;

    public static StatusVenda toEnum(Integer cod){
        if(cod == null){
            return null;
        }
        for(StatusVenda venda : StatusVenda.values()){
            if(cod.equals(venda.cod)){
                return venda;
            }
        }
        throw new IllegalArgumentException("id inválido: " + cod);
    }

}
