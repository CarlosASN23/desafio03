package br.com.compassuol.Desafio._3.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
public class ItemPedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long idItemPedido;

    @Getter
    @Setter
    @JsonIgnore
    @EmbeddedId
    private ItemPedidoPK id = new ItemPedidoPK();
    @Column(name = "Preco_Item",nullable = false)
    private Double precoDoItem;
    @Column(name = "Quantidade_Item",nullable = false)
    private Integer quantidadeDoItem;

    @Column(name = "Data_criacao_Item",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dataItemPedido;

    public ItemPedido(Long idItemPedido,Venda venda, Produto produto, Double precoDoItem, Integer quantidadeDoItem, Date dataItemPedido) {
        super();
        this.idItemPedido = idItemPedido;
        id.setVenda(venda);
        id.setProduto(produto);
        this.precoDoItem = precoDoItem;
        this.quantidadeDoItem = quantidadeDoItem;
        this.dataItemPedido = dataItemPedido;
    }

    @JsonIgnore
    public Venda getVenda() {
        return id.getVenda();
    }

    public Produto getProduto() {
        return id.getProduto();
    }

    @Override
    public String toString() {
        return "ItemPedido{" + id +
                " | Preco total Compra = R$" + precoDoItem +
                ", Quantidade Total do item = " + quantidadeDoItem;
    }

    public void atualizarEstoque (List< ItemPedido > itensPedido, Map< Long, Produto > estoque){
        for (ItemPedido item : itensPedido) {
            long idProduto = item.getProduto().getId();
            int quantidadeVendida = item.getProduto().getEstoque();

            if (estoque.containsKey(idProduto)) {
                Produto produto = estoque.get(idProduto);
                int quantidadeAtual = produto.getEstoque();
                if (quantidadeAtual >= quantidadeVendida) {
                    produto.setEstoque(quantidadeAtual - quantidadeVendida);
                    System.out.println("Estoque atualizado para o produto " + produto.getNome());
                } else {
                    System.out.println("Quantidade insuficiente em estoque para o produto " + produto.getNome());
                }
            }
        }
    }

    private double calcularPrecoTotal(List<ItemPedido> itensPedido) {
        double precoTotal = 0.0;
        for (ItemPedido item : itensPedido) {
            double precoItem = item.getProduto().getPreco() * item.quantidadeDoItem;
            precoTotal += precoItem;
        }
        return precoTotal;
    }

}
