package br.com.compassuol.Desafio._3.controller;

import br.com.compassuol.Desafio._3.model.ItemPedido;
import br.com.compassuol.Desafio._3.service.VendaService;
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
    public  ResponseEntity<List<ItemPedido>>buscarVendaPorId(@PathVariable Long id) {
        List<ItemPedido> items = vendaService.buscarVendaPorId(id);
        return ResponseEntity.ok(items);
    }


    // Método para filtrar por data
    @GetMapping("/filtrar")
    public  ResponseEntity<List<ItemPedido>>filtrarVendaPorData(@RequestParam("DataInicio")@DateTimeFormat(iso =DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
                                                                @RequestParam("DataFim")@DateTimeFormat(iso =DateTimeFormat.ISO.DATE)LocalDateTime fim) {

        List<ItemPedido> items = vendaService.filtroVendaPorData(inicio, fim);
        return ResponseEntity.ok(items);
    }

    // Método para gerar relatório semanal
    @GetMapping("/relatorio/semanal")
    public ResponseEntity<List<ItemPedido>>gerarRelatórioSemanal(){
        List<ItemPedido> items = vendaService.gerarRelatórioSemanal();
        return ResponseEntity.ok(items);
    }

    // Método para gerar relatório mensal
    @GetMapping("/relatorio/mensal")
    public ResponseEntity<List<ItemPedido>> gerarRelatorioMensal(@RequestParam Integer mes, @RequestParam Integer ano){

        List<ItemPedido> items = vendaService.gerarRelatorioMensal(mes, ano);
        return ResponseEntity.ok(items);
    }

    //Método para criar uma nova venda
    @PostMapping
    @Transactional
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
    public ResponseEntity cancelarVenda(@PathVariable Long id){
        vendaService.cancelarVenda(id);
        return ResponseEntity.noContent().build();
    }

    // Método para alterar uma venda
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ItemPedido> atualizarVenda(@PathVariable @RequestParam Long idVenda, @RequestParam @Valid Integer novoStatusVenda,
                                         @RequestParam @Valid Integer novaQuantidade){
        return ResponseEntity.ok(vendaService.atualizarVenda(idVenda,novoStatusVenda,novaQuantidade));
    }
}
