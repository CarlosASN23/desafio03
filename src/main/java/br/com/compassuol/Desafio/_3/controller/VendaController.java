package br.com.compassuol.Desafio._3.controller;

import br.com.compassuol.Desafio._3.dto.DadosProdutoDto;
import br.com.compassuol.Desafio._3.model.ItemPedido;
import br.com.compassuol.Desafio._3.model.Produto;
import br.com.compassuol.Desafio._3.model.Venda;
import br.com.compassuol.Desafio._3.service.VendaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
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
        List<ItemPedido> items = vendaService.buscarVendas();

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











}
