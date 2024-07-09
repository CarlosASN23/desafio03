package br.com.compassuol.Desafio._3.controller;

import br.com.compassuol.Desafio._3.dto.DadosProdutoDto;
import br.com.compassuol.Desafio._3.model.Produto;
import br.com.compassuol.Desafio._3.service.ProdutoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdutoController {


    @Autowired
    private ProdutoService service;

    // Método para Lista os produtos ativos do banco
    @GetMapping
    public ResponseEntity<List<Produto>> buscarProduto(){
        List<Produto> produtos = service.buscarProduto();

        if(!produtos.isEmpty()){
            return ResponseEntity.ok(produtos);
        }else{
            return ResponseEntity.noContent().build();
        }
    }

    // Método para buscar produto pelo ID
    @GetMapping("/{id}")
    public  ResponseEntity<DadosProdutoDto> buscarProdutoPorId(@PathVariable Long id) {
        DadosProdutoDto produto = service.buscarProdutoPorId(id);
        return ResponseEntity.ok(produto);
    }

    // Método para adicionar um novo produto ao banco de dados
    @PostMapping
    @Transactional
    public ResponseEntity cadastrarProduto(@RequestBody @Valid Produto dados, UriComponentsBuilder uriBuilder){

        var prod = service.cadastrarProduto(dados);
        var uri = uriBuilder.path("/produto/{id}").buildAndExpand(prod.getId()).toUri();
        return ResponseEntity.created(uri).body(prod);
    }

    // Método para atualizar um produto
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizarProduto(@RequestBody @Valid DadosProdutoDto dadosProduto,@PathVariable Long id){
        return ResponseEntity.ok(service.atualizarProduto(dadosProduto, dadosProduto.id()));

    }

    // Método para inativar um produto no banco
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity inativarProduto(@PathVariable Long id){
        service.inativarProduto(id);
        return ResponseEntity.noContent().build();
    }

}
