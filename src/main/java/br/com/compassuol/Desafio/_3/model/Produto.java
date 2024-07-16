package br.com.compassuol.Desafio._3.model;

import br.com.compassuol.Desafio._3.dto.DadosProdutoDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "produto")
public class Produto implements Serializable {

    private static final long serialVersionUID = 5620606054205035360L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long idProduto;
    @Column(name ="nome",nullable = false, unique = true)
    private String nome;
    @Column(name ="preco",nullable = false)
    private Double preco;
    @Column(name = "estoque",nullable = false)
    private Integer estoque = 0;
    @Column(name="ativo",nullable = false)
    private Boolean ativo = true;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,mappedBy= "produto", cascade = CascadeType.ALL)
    private List<ItemPedido> itens = new ArrayList<>();

    @Override
    public String toString() {
        return "Produto{"+"ID= " + idProduto +
                ", Nome= " + nome  +
                ", Preço= R$" + preco +
                ", Ativo= " + ativo +
                ", Estoque= " + estoque +"}\n";
    }

    public DadosProdutoDto atualizarInformacoes(DadosProdutoDto produto) {

        if(produto.nome() != null){
            this.nome = produto.nome();
        }
        if(produto.preco() != null && produto.preco() >= 0){
            this.preco = produto.preco();
        }if(produto.ativo() != null){
            this.ativo = produto.ativo();
        }if(produto.estoque() != null){
            this.estoque = produto.estoque();
        }
        return produto;
    }

    public void excluir() {
        this.ativo = false;
    }
}
