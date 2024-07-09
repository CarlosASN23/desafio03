package br.com.compassuol.Desafio._3.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class ItemPedidoPK implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManyToOne
    @JoinColumn(name="venda_id")
    private Venda venda;

    @ManyToOne
    @JoinColumn(name="produto_id")
    private Produto produto;

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public String toString() {
        return " Venda = ID: " + venda.getId() + ",Data " + venda.getDataCriacao() +
                ",Status Venda " + venda.getStatusVenda()+
                ", Nome do produto = " + produto.getNome() + ", Preço = R$" + produto.getPreco();
    }
}
