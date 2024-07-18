package br.com.compassuol.Desafio._3.controller;

import br.com.compassuol.Desafio._3.model.ItemPedido;
import br.com.compassuol.Desafio._3.service.VendaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/venda")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    // Método para buscar todas as vendas
    @GetMapping
    @Operation(summary = "Realizar a busca das vendas realizadas")
    public ResponseEntity<List<ItemPedido>> buscarVendas(){
        List<ItemPedido> items = vendaService.buscarVenda();

        if(!items.isEmpty()){
            return ResponseEntity.ok(items);
        }else{
            return ResponseEntity.noContent().build();
        }
    }

    // Método para buscar produto pelo ID
    @GetMapping("/{id}")
    @Operation(summary = "Realizar a busca de uma venda através do ID")
    public  ResponseEntity<List<ItemPedido>>buscarVendaPorId(@PathVariable Long id) {
        List<ItemPedido> items = vendaService.buscarVendaPorId(id);
        return ResponseEntity.ok(items);
    }


    // Método para filtrar por data
    @GetMapping("/filtrar")
    @Operation(summary = "Realizar um filtro passando duas datas como parâmtro")
    public  ResponseEntity<List<ItemPedido>>filtrarVendaPorData(@RequestParam("DataInicio")@DateTimeFormat(iso =DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
                                                                @RequestParam("DataFim")@DateTimeFormat(iso =DateTimeFormat.ISO.DATE)LocalDateTime fim) {

        List<ItemPedido> items = vendaService.filtroVendaPorData(inicio, fim);
        return ResponseEntity.ok(items);
    }

    // Método para gerar relatório semanal
    @GetMapping("/relatorio/semanal")
    @Operation(summary = "Gerar um relatório com as vendas realizadas durante o periodo de uma seman")
    public ResponseEntity<List<ItemPedido>>gerarRelatórioSemanal(){
        List<ItemPedido> items = vendaService.gerarRelatórioSemanal();
        return ResponseEntity.ok(items);
    }

    // Método para gerar relatório mensal
    @GetMapping("/relatorio/mensal")
    @Operation(summary = "Gerar um relatório com as vendas realizadas durante o mês")
    public ResponseEntity<List<ItemPedido>> gerarRelatorioMensal(@RequestParam Integer mes, @RequestParam Integer ano){

        List<ItemPedido> items = vendaService.gerarRelatorioMensal(mes, ano);
        return ResponseEntity.ok(items);
    }

    //Método para criar uma nova venda
    @PostMapping
    @Transactional
    @Operation(summary = "Criar uma nova venda, passando os ID do produto e usuario")
    public ResponseEntity<ItemPedido> criarNovaVenda(@RequestParam @Valid Long idProduto,
                                                     @RequestParam @Valid Long idUsuario,
                                                     UriComponentsBuilder uriBuilder){

        ItemPedido itemPedido = vendaService.criarVenda(idProduto,idUsuario);
        var uri = uriBuilder.path("/venda/{id}").buildAndExpand(itemPedido.getVenda().getIdVenda()).toUri();
        return ResponseEntity.created(uri).body(itemPedido);
    }

    // Método para cancelar venda
    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Endpoint para cancelar uma venda e mudar seu status para CANCELADO")
    public ResponseEntity cancelarVenda(@PathVariable Long id){
        vendaService.cancelarVenda(id);
        return ResponseEntity.noContent().build();
    }

    // Método para alterar uma venda
    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Endpoint para alterar uma venda")
    public ResponseEntity<ItemPedido> atualizarVenda(@PathVariable @RequestParam Long idVenda, @RequestParam @Valid Integer novoStatusVenda,
                                                     @RequestParam @Valid Integer novaQuantidade){
        return ResponseEntity.ok(vendaService.atualizarVenda(idVenda,novoStatusVenda,novaQuantidade));
    }
}