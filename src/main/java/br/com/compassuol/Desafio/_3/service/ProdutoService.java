package br.com.compassuol.Desafio._3.service;

import br.com.compassuol.Desafio._3.dto.DadosProdutoDto;
import br.com.compassuol.Desafio._3.exception.DuplicatedObjectException;
import br.com.compassuol.Desafio._3.exception.ObjectNotFoundException;
import br.com.compassuol.Desafio._3.model.Produto;
import br.com.compassuol.Desafio._3.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;


    @Cacheable("produtos")
    public List<Produto> buscarProduto() {
        try {
            return produtoRepository.findAllByAtivoTrue();
        }catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Produtos não encontrados");
        }
    }

    @Cacheable("produtos_por_id")
    public DadosProdutoDto buscarProdutoPorId(Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);

        if (produto.isPresent()) {
            Produto p = produto.get();
            return new DadosProdutoDto(p.getIdProduto(),p.getNome(), p.getPreco(),p.getAtivo(),p.getEstoque());
        } throw new ObjectNotFoundException("Produto não encontrado para o ID: " + id);

    }

    @CacheEvict("cadastro_produto")
    public Produto cadastrarProduto(Produto produto) {

        try {
            return produtoRepository.save(produto);

        }catch (RuntimeException e) {

            throw new DuplicatedObjectException(e.getMessage());

        }
    }

    @CacheEvict("atualizar_produto")
    public DadosProdutoDto atualizarProduto(DadosProdutoDto produto,Long id) {
        try {
            var prod = produtoRepository.getReferenceById(id);
            return prod.atualizarInformacoes(produto);
        }catch (Exception e) {
            throw new ObjectNotFoundException("Produto não encontrado para o ID:" + produto.id(), e);
        }
    }

    @CacheEvict("inativar_produto")
    public void inativarProduto(Long id) {
        try {
            var prod = produtoRepository.getReferenceById(id);
            prod.excluir();
        }catch (RuntimeException e){
            throw new ObjectNotFoundException("Produto não encontrado pelo ID:" + id, e);
        }
    }
}
