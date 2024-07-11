package br.com.compassuol.Desafio._3.service;

import br.com.compassuol.Desafio._3.dto.DadosItemPedidoDto;
import br.com.compassuol.Desafio._3.dto.DadosProdutoDto;
import br.com.compassuol.Desafio._3.dto.DadosVendaDto;
import br.com.compassuol.Desafio._3.exception.InvalidDateException;
import br.com.compassuol.Desafio._3.exception.NoItemInSalesException;
import br.com.compassuol.Desafio._3.exception.NoProdutcAtiveExcetion;
import br.com.compassuol.Desafio._3.exception.ObjectNotFoundException;
import br.com.compassuol.Desafio._3.model.ItemPedido;
import br.com.compassuol.Desafio._3.model.Produto;
import br.com.compassuol.Desafio._3.model.Venda;
import br.com.compassuol.Desafio._3.model.enums.StatusVenda;
import br.com.compassuol.Desafio._3.repository.ItemPedidoRepository;
import br.com.compassuol.Desafio._3.repository.ProdutoRepository;
import br.com.compassuol.Desafio._3.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    private ProdutoService produtoService;

    @Autowired
    public VendaService(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    public List<ItemPedido> buscarVendas(){
        try{
            return itemPedidoRepository.findAll();
        }catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Não foi possivel encontrar as vendas");
        }
    }

    public List<ItemPedido> buscarVendaPorId(Long id){

        Optional<Venda> venda = vendaRepository.findById(id);

        if(venda.isPresent()){
            List<ItemPedido> itens = itemPedidoRepository.exibirItensPorVendaId(id);
            return itens;
        }throw  new ObjectNotFoundException("Venda não encontradas para o id: " + id);

    }

    public ItemPedido criarVenda(Long idProduto){

        Optional<Produto>produto = produtoRepository.findById(idProduto);
        if(produto.isPresent()) {

            Produto p = produto.get();

            var produtos = new DadosProdutoDto(p.getIdProduto(), p.getNome(), p.getPreco(), p.getAtivo(), p.getEstoque());

            if (p.getAtivo() == true && p.getEstoque() > 0) {

                // Instacianção de uma nova venda -
                // Onde toda venda tem como padrão o status Efetivada (por não estar considerando pagamentos)
                Venda venda = new Venda();
                venda.setStatusVenda(StatusVenda.EFETIVADA);
                venda.setDataCriacao(LocalDateTime.now());

                // Instanciação de um novo item pedido a ser alocado a venda
                ItemPedido itemPedido = new ItemPedido();
                itemPedido.setDataItemPedido(LocalDateTime.now());
                itemPedido.setQuantidadeDoItem(1);
                itemPedido.setVenda(venda);
                itemPedido.setPrecoDoItem(p.getPreco() * itemPedido.getQuantidadeDoItem());
                itemPedido.setProduto(p);

                // Atribuindo o valor todal da venda
                venda.setValorVenda(p.getPreco() * itemPedido.getQuantidadeDoItem());
                vendaRepository.save(venda);

                itemPedidoRepository.save(itemPedido);
                // Bloco para atualizar o estoque do produto após a venda
                var novoEstoque = produtos.estoque() - itemPedido.getQuantidadeDoItem();
                produtos = new DadosProdutoDto(p.getIdProduto(), p.getNome(), p.getPreco(), p.getAtivo(), novoEstoque);

                // Salvando atualizações do estoque do produto
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
    }

    public List<ItemPedido> filtroVendaPorData(LocalDateTime inicio, LocalDateTime fim){
        
        if(inicio.isAfter(LocalDateTime.now())){

            throw new InvalidDateException("A data inicial deve estar no passado");

        }else if(inicio.isAfter(fim)){

            throw new InvalidDateException("A data inicial deve ser antes da data final");
        }

        return itemPedidoRepository.findByDataItemPedidoBetween(inicio,fim);
    }

    public List<ItemPedido> gerarRelatórioSemanal(){
        // 1. Obter as vendas da semana atual (ou do período desejado)
        LocalDateTime dataInicioSemana = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); // Início da semana
        LocalDateTime dataFimSemana = LocalDateTime.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX); // Fim da semana

        List<ItemPedido> vendaSemana = itemPedidoRepository.findByDataItemPedidoBetween(dataInicioSemana,dataFimSemana);

        return vendaSemana;
    }

    public List<ItemPedido>gerarRelatorioMensal(Integer mes, Integer ano){

        // Primeiro, crie as datas de início e fim do mês desejado
        LocalDate dataInicio = LocalDate.of(ano,mes,1);
        LocalDate dataFim = dataInicio.withDayOfMonth(dataInicio.lengthOfMonth());

        // Consulte as vendas dentro do intervalo de datas
        LocalDateTime inicioMes = dataInicio.atStartOfDay();
        LocalDateTime fimMes = dataFim.atTime(LocalTime.MAX);

        List<ItemPedido> itemPedidos = itemPedidoRepository.findByDataItemPedidoBetween(inicioMes,fimMes);

        return itemPedidos;
    }

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

    public DadosItemPedidoDto atualizarVenda(DadosItemPedidoDto dadosItemPedidoDto,
                                             DadosVendaDto dadosVendaDto ,Long id){

        try{
            var venda = vendaRepository.findById(id);

            if(venda.isPresent()){

                Venda v = venda.get();

                var itemPedido = itemPedidoRepository.findById(venda.get().getIdVenda());
                ItemPedido item = itemPedido.get();

                v.atualizarInformacoes(dadosVendaDto);
                return itemPedido.orElseThrow().atualizarInformacoes(dadosItemPedidoDto);

            }else{
                throw new ObjectNotFoundException("Não foi possivel encontrar a venda para o ID" + id);
            }
        }catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Não foi possivel localizar a venda");
        }
    }

}
