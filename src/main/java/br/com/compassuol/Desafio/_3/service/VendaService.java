package br.com.compassuol.Desafio._3.service;

import br.com.compassuol.Desafio._3.dto.DadosItemPedidoDto;
import br.com.compassuol.Desafio._3.dto.DadosProdutoDto;
import br.com.compassuol.Desafio._3.dto.DadosVendaDto;
import br.com.compassuol.Desafio._3.exception.*;
import br.com.compassuol.Desafio._3.exception.NullPointerException;
import br.com.compassuol.Desafio._3.model.ItemPedido;
import br.com.compassuol.Desafio._3.model.Produto;
import br.com.compassuol.Desafio._3.model.Venda;
import br.com.compassuol.Desafio._3.model.enums.StatusVenda;
import br.com.compassuol.Desafio._3.repository.ItemPedidoRepository;
import br.com.compassuol.Desafio._3.repository.ProdutoRepository;
import br.com.compassuol.Desafio._3.repository.VendaRepository;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;


    @Cacheable("vendas") // @Cacheable não apresenta suporte para o tipo LocalDateTime
    public List<ItemPedido> buscarVenda() {
        try {
            return itemPedidoRepository.findAll();
        }catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Produtos não encontrados");
        }
    }

    @Cacheable("buscar_venda_por_id")
    public List<ItemPedido> buscarVendaPorId(Long id){
        try{

            Optional<Venda> venda = vendaRepository.findById(id);

            if(venda.isPresent()){
                List<ItemPedido> itens = itemPedidoRepository.exibirItensPorVendaId(id);
                return itens;
            }throw  new ObjectNotFoundException("Venda não encontradas para o id: " + id);

        }catch (ObjectNotFoundException e){

            throw new ObjectNotFoundException("Produto não encontrado");
        }
    }

    @CacheEvict("criar_venda")
    public ItemPedido criarVenda(Long idProduto){

        try{
            Optional<Produto>produto = produtoRepository.findById(idProduto);

            if(produto.isPresent()) {

                Produto p = produto.get();

                var produtos = new DadosProdutoDto(p.getIdProduto(), p.getNome(), p.getPreco(), p.getAtivo(), p.getEstoque());

                if (p.getAtivo() == true && p.getEstoque() > 0) {

                    Venda venda = new Venda();
                    venda.setStatusVenda(StatusVenda.EFETIVADA);
                    venda.setDataCriacao(LocalDateTime.now());


                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setDataItemPedido(LocalDateTime.now());
                    itemPedido.setQuantidadeDoItem(1);
                    itemPedido.setVenda(venda);
                    itemPedido.setPrecoDoItem(p.getPreco() * itemPedido.getQuantidadeDoItem());
                    itemPedido.setProduto(p);

                    venda.setValorVenda(p.getPreco() * itemPedido.getQuantidadeDoItem());
                    vendaRepository.save(venda);

                    itemPedidoRepository.save(itemPedido);
                    // Bloco para atualizar o estoque do produto após a venda
                    var novoEstoque = produtos.estoque() - itemPedido.getQuantidadeDoItem();
                    produtos = new DadosProdutoDto(p.getIdProduto(), p.getNome(), p.getPreco(), p.getAtivo(), novoEstoque);

                    p.atualizarInformacoes(produtos);
                    produtoRepository.save(p);

                    return itemPedido;

                }else if(p.getAtivo() == true && p.getEstoque()<=0){
                    throw new NoItemInSalesException("Sem item em estoque para realizar a venda");
                }else{
                    throw new NoProdutcAtiveExcetion("Produto deve estar ativo");
                }
            }
            return null;
        }catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Não foi possivel encontrar o Id do Produto" + idProduto);
        }catch (NullPointerException e){
            throw new NullPointerException("Entrada de dados inválida/nulos para gerar um nova venda");
        }
    }

    @Cacheable("filtrar_venda_por_data")
    public List<ItemPedido> filtroVendaPorData(LocalDateTime inicio, LocalDateTime fim) {
        try {
            if (inicio.isAfter(LocalDateTime.now())) {

                throw new InvalidDateException("A data inicial deve estar no passado");

            } else if (inicio.isAfter(fim)) {

                throw new InvalidDateException("A data inicial deve ser antes da data final");
            }
            return itemPedidoRepository.findByDataItemPedidoBetween(inicio, fim);
        }catch(InvalidDateException e) {
            throw new InvalidDateException(e.getMessage());
        }
    }

    @Cacheable("gerar_relatorio_semanal")
    public List<ItemPedido> gerarRelatórioSemanal(){

        try{
            // 1. Obter as vendas da semana atual (ou do período desejado)
            LocalDateTime dataInicioSemana = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); // Início da semana
            LocalDateTime dataFimSemana = LocalDateTime.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX); // Fim da semana

            List<ItemPedido> vendaSemana = itemPedidoRepository.findByDataItemPedidoBetween(dataInicioSemana,dataFimSemana);

            return vendaSemana;
        }catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Não foi possivel localizar as vendas");
        }

    }

    @Cacheable("gerar_relatorio_mensal")
    public List<ItemPedido>gerarRelatorioMensal(Integer mes, Integer ano){

        try{
            // Primeiro, crie as datas de início e fim do mês desejado
            LocalDate dataInicio = LocalDate.of(ano,mes,1);
            LocalDate dataFim = dataInicio.withDayOfMonth(dataInicio.lengthOfMonth());

            // Consulte as vendas dentro do intervalo de datas
            LocalDateTime inicioMes = dataInicio.atStartOfDay();
            LocalDateTime fimMes = dataFim.atTime(LocalTime.MAX);

            List<ItemPedido> itemPedidos = itemPedidoRepository.findByDataItemPedidoBetween(inicioMes,fimMes);

            return itemPedidos;

        }catch (InvalidDateException e){

            throw new InvalidDateException("Entrada de datas inválidas");

        }catch (InputMismatchException e){

            throw  new InputMismatchException("Informe um valor de datas válidos ");
        }

    }

    @CacheEvict("cancelar_venda")
    public void cancelarVenda(Long id){
        try{
            Optional<Venda> venda  = vendaRepository.findById(id);
            if(venda.isPresent()){
                Venda v = venda.get();
                var dadosVenda = new DadosVendaDto(v.getIdVenda(), v.getDataCriacao(), v.getStatusVenda(), v.getValorVenda());

                // Instanciando o itemPedido
                List<ItemPedido> items = itemPedidoRepository.exibirItensPorVendaId(id);
                ItemPedido itemPedido = items.get(0);

                var dadosItemPedido = new DadosItemPedidoDto(itemPedido.getIdItemPedido(), itemPedido.getVenda(),
                        itemPedido.getProduto(), itemPedido.getPrecoDoItem(),
                        itemPedido.getQuantidadeDoItem(), itemPedido.getDataItemPedido());

                // Instanciando o produto
                Optional<Produto> produto = produtoRepository.findById(itemPedido.getProduto().getIdProduto());
                Produto p = produto.get();
                var produtos = new DadosProdutoDto(p.getIdProduto(), p.getNome(), p.getPreco(), p.getAtivo(), p.getEstoque());

                v.setValorVenda(0.0);
                v.setStatusVenda(StatusVenda.CANCELADA);
                DadosVendaDto dados = new DadosVendaDto(v.getIdVenda(), v.getDataCriacao(), v.getStatusVenda(), v.getValorVenda());

                v.atualizarInformacoes(dados);
                vendaRepository.save(v);

                // Bloco para atualizar o estoque do produto após cancelamento da venda
                var novoEstoque = produtos.estoque() + itemPedido.getQuantidadeDoItem();
                produtos = new DadosProdutoDto(p.getIdProduto(), p.getNome(), p.getPreco(), p.getAtivo(), novoEstoque);
                p.atualizarInformacoes(produtos);
                produtoRepository.save(p);

                // Zerar a quantidade comprada do produto e o valor
                itemPedido.setQuantidadeDoItem(0);
                itemPedido.setPrecoDoItem(0.0);
                DadosItemPedidoDto dadosItem = new DadosItemPedidoDto(itemPedido.getIdItemPedido(), itemPedido.getVenda(),
                        itemPedido.getProduto(), itemPedido.getPrecoDoItem(),
                        itemPedido.getQuantidadeDoItem(), itemPedido.getDataItemPedido());

                itemPedido.atualizarInformacoes(dadosItem);
                itemPedidoRepository.save(itemPedido);

            }else{
                throw new ObjectNotFoundException("Não foi possivel encontrar a venda para o ID" + id);
            }
        }catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Venda não encontrada");
        }
    }

    @CacheEvict("atualizar_vendas")
    public ItemPedido atualizarVenda(Long idVenda,Integer novoStatusVenda,Integer novaQuantidade) {
        try {
            Optional<Venda> venda = vendaRepository.findById(idVenda);
            if (venda.isPresent()) {

                Venda v = venda.get();
                var dadosVenda = new DadosVendaDto(v.getIdVenda(), v.getDataCriacao(), v.getStatusVenda(), v.getValorVenda());

                switch (novoStatusVenda){
                    case 1:
                        v.setStatusVenda(StatusVenda.PENDENTE);
                        break;
                    case 2:
                        v.setStatusVenda(StatusVenda.EFETIVADA);
                        break;
                    default:
                        throw new InputMismatchException("Digite um valor válido");
                }

                // Instanciando o itemPedido
                List<ItemPedido> items = itemPedidoRepository.exibirItensPorVendaId(idVenda);
                ItemPedido itemPedido = items.get(0);

                var dadosItemPedido = new DadosItemPedidoDto(itemPedido.getIdItemPedido(), itemPedido.getVenda(),
                        itemPedido.getProduto(), itemPedido.getPrecoDoItem(),
                        itemPedido.getQuantidadeDoItem(), itemPedido.getDataItemPedido());

                // Instanciando o produto
                Optional<Produto> produto = produtoRepository.findById(itemPedido.getProduto().getIdProduto());
                Produto p = produto.get();
                var produtos = new DadosProdutoDto(p.getIdProduto(), p.getNome(), p.getPreco(), p.getAtivo(), p.getEstoque());

                // Verificar se há estoque suficiente para a alteração da venda
                if(novaQuantidade>0 && novaQuantidade <= p.getEstoque()){

                    // Voltar o estoque para não haver duas saidas do estoque do produto com a venda anterior
                    var novoEstoque = produtos.estoque() + itemPedido.getQuantidadeDoItem();
                    produtos = new DadosProdutoDto(p.getIdProduto(),p.getNome(),p.getPreco(),p.getAtivo(),novoEstoque);
                    p.atualizarInformacoes(produtos);
                    produtoRepository.save(p);

                    var valorTotal = novaQuantidade * p.getPreco();

                    // Instanciar os novos dados do itemPedido
                    DadosItemPedidoDto item = new DadosItemPedidoDto(itemPedido.getIdItemPedido(), itemPedido.getVenda(),
                            itemPedido.getProduto(), valorTotal, novaQuantidade,itemPedido.getDataItemPedido());

                    v.setValorVenda(valorTotal);

                    DadosVendaDto dados = new DadosVendaDto(v.getIdVenda(),v.getDataCriacao(),v.getStatusVenda(),valorTotal);

                    // Atualizar o estoque do produto após atualização da venda
                    var atualizacao = produtos.estoque() - novaQuantidade;
                    produtos = new DadosProdutoDto(p.getIdProduto(), p.getNome(), p.getPreco(), p.getAtivo(), atualizacao);
                    p.atualizarInformacoes(produtos);
                    produtoRepository.save(p);

                    DadosItemPedidoDto dadosItem = new DadosItemPedidoDto(itemPedido.getIdItemPedido(),itemPedido.getVenda(),
                            itemPedido.getProduto(), valorTotal,
                            novaQuantidade,itemPedido.getDataItemPedido());

                    // Atualizar informações da venda e do item de pedido
                    v.atualizarInformacoes(dados);
                    itemPedido.atualizarInformacoes(dadosItem);

                    // Salvar as alterações no banco
                    vendaRepository.save(v);
                    itemPedidoRepository.save(itemPedido);
                    return itemPedido;

                }else if (p.getEstoque() != 0 && p.getEstoque() < novaQuantidade){

                    // Irá atribuir a quantidade maxima em estoque do produto como a quantidade a comprar
                   var novaQuantidadeCompra =  p.getEstoque();

                    // Voltar o estoque para não haver duas saidas do estoque do produto com a venda anterior
                    var novoEstoque = produtos.estoque() + itemPedido.getQuantidadeDoItem();
                    produtos = new DadosProdutoDto(p.getIdProduto(),p.getNome(),p.getPreco(),p.getAtivo(),novoEstoque);
                    p.atualizarInformacoes(produtos);
                    produtoRepository.save(p);

                    // Calcular o novo valor total da compra
                    var valorTotal = novaQuantidadeCompra * p.getPreco();

                    v.setValorVenda(valorTotal);

                    // Instancia os novos dados da venda
                    DadosVendaDto dados = new DadosVendaDto(v.getIdVenda(),v.getDataCriacao(),v.getStatusVenda(),valorTotal);

                    // Atualizar o estoque do produto após atualização da venda
                    var atualizacao = produtos.estoque() - novaQuantidade;
                    produtos = new DadosProdutoDto(p.getIdProduto(), p.getNome(), p.getPreco(), p.getAtivo(), atualizacao);
                    p.atualizarInformacoes(produtos);
                    produtoRepository.save(p);

                    // Instanciar os novos dados do itemPedido
                    DadosItemPedidoDto dadosItem = new DadosItemPedidoDto(itemPedido.getIdItemPedido(),itemPedido.getVenda(),
                            itemPedido.getProduto(), valorTotal,
                            novaQuantidade,itemPedido.getDataItemPedido());

                    // Atualizar informações da venda e do item de pedido
                    v.atualizarInformacoes(dadosVenda);
                    itemPedido.atualizarInformacoes(dadosItem);

                    // Salvar as alterações no banco
                    vendaRepository.save(v);
                    itemPedidoRepository.save(itemPedido);
                    return itemPedido;

                }else{
                    throw new InputMismatchException("Não foi possivel realizar a alteração na venda");
                }
            }else{
                throw new ObjectNotFoundException("Não foi possivel localizar a venda de id:" + idVenda);
            }
        } catch (ObjectNotFoundException e) {

            throw new ObjectNotFoundException("Não foi possivel localizar a venda pelo id");

        }catch (NullPointerException e){

            throw new NullPointerException("Há valores nulos/inválidos sendo aplicados");

        }
    }
}
