package br.com.compassuol.Desafio._3.model;

import br.com.compassuol.Desafio._3.dto.itemPedido.DadosItemPedidoDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ItemPedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long idItemPedido;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name="idVenda")
    private Venda venda;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name="idProduto")
    private Produto produto;

    @Column(name = "Preco_Item",nullable = false)
    private Double precoDoItem;
    @Column(name = "Quantidade_Item",nullable = false)
    private Integer quantidadeDoItem;

    @CurrentTimestamp
    @Column(name = "Data_criacao_Item",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime dataItemPedido;

    public ItemPedido(Venda venda,Produto produto,Double precoDoItem, Integer quantidadeDoItem) {
        super();
        this.venda = venda;
        this.produto = produto;
        this.precoDoItem = precoDoItem;
        this.quantidadeDoItem = quantidadeDoItem;
    }

    @Override
    public String toString() {
        return "ItemPedido{ id: " + idItemPedido + "{Venda id: " +
                venda.getIdVenda() + ",Data " + venda.getDataCriacao() +
                ", Status Venda " + venda.getStatusVenda()+
                ", Id usuario: " + venda.getUsuario().getId() +
                ", || Nome do produto = " + produto.getNome() +
                ", Preço Unitário = R$" + produto.getPreco() +
                ", || Preco total Compra = R$" + precoDoItem +
                ", Quantidade Total do item = " + quantidadeDoItem +
                ", Data do Pedido do item = " + dataItemPedido;
    }

    public DadosItemPedidoDto atualizarInformacoes(DadosItemPedidoDto dadosItem) {
        if(dadosItem.precoItem() != null){
            this.precoDoItem = dadosItem.precoItem();
        }
        if(dadosItem.quantidadeItem() != null){
            this.quantidadeDoItem = dadosItem.quantidadeItem();
        }
        return dadosItem;
    }
}
