package br.com.compassuol.Desafio._3.service;

import br.com.compassuol.Desafio._3.dto.DadosProdutoDto;
import br.com.compassuol.Desafio._3.exception.InvalidDateException;
import br.com.compassuol.Desafio._3.exception.NoItemInSalesException;
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
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import static java.util.Calendar.DAY_OF_WEEK;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

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

    public Venda atualizarVenda(Long id){
        return null;
    }


    public Venda realizarVenda(List<ItemPedido> itens, Long idProduto){

        if(itens.isEmpty()){
            throw new NoItemInSalesException("A venda deve conter pelo menos 1 produto");
        }

        Optional<Produto> produto = produtoRepository.findById(idProduto);
        if(produto.isPresent()){

            Produto p = produto.get();
            var produtos = new DadosProdutoDto(p.getIdProduto(),p.getNome(),p.getPreco(),p.getAtivo(),p.getEstoque());

            Venda venda = new Venda();
            venda.setDataCriacao(LocalDateTime.now());
            venda.setStatusVenda(StatusVenda.EFETIVADA);
            ItemPedido item = new ItemPedido();

            venda.setValorVenda(p.getPreco() * item.getQuantidadeDoItem());
            vendaRepository.save(venda);

            item.setProduto(p);
            itemPedidoRepository.save(item);

            return venda;


        }else{
            throw new ObjectNotFoundException("Não foi possivel associar o id do produto a venda");
        }

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

}
