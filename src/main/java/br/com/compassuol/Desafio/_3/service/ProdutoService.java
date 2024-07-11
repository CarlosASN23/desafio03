package br.com.compassuol.Desafio._3.service;

import br.com.compassuol.Desafio._3.dto.DadosProdutoDto;
import br.com.compassuol.Desafio._3.exception.DuplicatedObjectException;
import br.com.compassuol.Desafio._3.exception.InputMismatchException;
import br.com.compassuol.Desafio._3.exception.ObjectNotFoundException;
import br.com.compassuol.Desafio._3.model.Produto;
import br.com.compassuol.Desafio._3.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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

        try{
            Optional<Produto> produto = produtoRepository.findById(id);

            if (produto.isPresent()) {
                Produto p = produto.get();
                var produtos = new DadosProdutoDto(p.getIdProduto(),p.getNome(),p.getPreco(),p.getAtivo(),p.getEstoque());
                return produtos;

            } throw new ObjectNotFoundException("Produto não encontrado para o ID: " + id);

        }catch (ObjectNotFoundException e){

            throw new ObjectNotFoundException("Produto não encontrado para o ID: " + id);

        }catch (InputMismatchException e){

            throw new InputMismatchException("Entrada de dados inválida, informe um id válido");
        }

    }

    @CacheEvict("cadastro_produto")
    public Produto cadastrarProduto(Produto produto) {

        try {
            return produtoRepository.save(produto);

        }catch (DuplicatedObjectException e) {
            throw new DuplicatedObjectException("Produto ja se encontra cadastrado no banco de dados");
        }catch (InputMismatchException e){
            throw new InputMismatchException("Informe dados válidos para cadastrar um produto");
        }
    }

    @CacheEvict("atualizar_produto")
    public DadosProdutoDto atualizarProduto(DadosProdutoDto produto,Long id) {
        try {
            var prod = produtoRepository.getReferenceById(id);
            return prod.atualizarInformacoes(produto);
        }catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Produto não encontrado para o ID:" + produto.id());
        }catch (InputMismatchException e){

            throw new InputMismatchException("Entrada de dados inválida, informe um id válido");
        }
    }

    @CacheEvict("inativar_produto")
    public void inativarProduto(Long id) {
        try {
            var prod = produtoRepository.getReferenceById(id);
            prod.excluir();
        }catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Produto não encontrado pelo ID:" + id);
        }catch (InputMismatchException e){

            throw new InputMismatchException("Entrada de dados inválida, informe um id válido");
        }
    }
}
