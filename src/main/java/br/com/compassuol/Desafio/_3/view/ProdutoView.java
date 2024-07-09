package br.com.compassuol.Desafio._3.view;

import br.com.compassuol.Desafio._3.dto.DadosProdutoDto;
import br.com.compassuol.Desafio._3.exception.ObjectNotFoundException;
import br.com.compassuol.Desafio._3.model.Produto;
import br.com.compassuol.Desafio._3.repository.ProdutoRepository;

import java.util.*;

public class ProdutoView {

    Scanner sc = new Scanner(System.in);

    // Repositories
    private ProdutoRepository produtoRepository;

    // DTOs
    private List<DadosProdutoDto> dadosProduto = new ArrayList<>();
    private DadosProdutoDto dados;


    //Model
    private List<Produto> produtos = new ArrayList<>();
    private Produto produto;

    public ProdutoView(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;

    }

    public void exibirMenu() {
        var opcao = -1;

        while (opcao != 0) {
            var menu = """
                \n======== MENU OPÇÕES ========
                
                1 - Cadastrar Produtos
                2 - Listar produtos ativos
                3 - Buscar produto ativos por ID
                4 - Atualizar produto
                5 - Inativar produto
                    
                0 - Sair
                ==============================
                """;
            System.out.println(menu);
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarProduto(produto);
                    break;

                case 2:
                    listarTodosOsProdutos(produtos);
                    break;

                case 3:
                    buscarProdutoPorId();
                    break;

                case 4:
                    atualizarProduto(dados);
                    break;

                case 5:
                    inativarProduto();
                    break;

                case 0:
                    System.out.println("Encerrando aplicação....");
                    break;

                default:
                    System.out.println("Opção inválida");
            }
        }
    }
    // Método para cadastrar um novo produto
    private void cadastrarProduto(Produto produto) {

        while (true) {
            Produto prod = new Produto();

            System.out.println("Informe o nome do produto: ");
            prod.setNome(sc.nextLine());

            if (produtoRepository.findByNomeContainingIgnoreCase(prod.getNome()).isPresent()) {
                System.out.println("O produto já está cadastrado no banco de dados.");
                exibirMenu();
            } else {
                System.out.println("Informe o valor do produto: ");
                prod.setPreco(sc.nextDouble());

                if (prod.getPreco() <= 0) {
                    System.out.println("O preço do produto deve ser maior que zero");
                    exibirMenu();
                    continue;
                }

                System.out.println("Informe se o produto vai estar ativo ou não (true/false): ");
                prod.setAtivo(sc.nextBoolean());

                System.out.println("Informe a quantidade do produto a ser alocado no estoque: ");
                prod.setEstoque(sc.nextInt());

                if (prod.getEstoque() < 0) {
                    System.out.println("A quantidade a ser inserida no estoque deve ser maior que zero");
                    exibirMenu();
                    continue;
                }

                sc.nextLine(); // Descarta a quebra de linha pendente
                produtoRepository.save(prod);
                System.out.println("Produto cadastrado com sucesso: " + prod);
            }

            System.out.println("Deseja cadastrar outro produto? (S/N)");
            String resposta = sc.nextLine();
            if (!resposta.equalsIgnoreCase("S")) {
                break;
            }
        }
    }

    // Método para listar todos os produtos ativos
    private void listarTodosOsProdutos(List<Produto> produtos) {
        try{

            List<Produto> prod;
            prod = produtoRepository.findAllByAtivoTrue();
            System.out.println(prod);

        }catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Lista de produtos esta vazia");
        }
    }

    //Método para buscar produto pelo ID
    private void buscarProdutoPorId() {
        try {
            System.out.println("Entre com o ID do produto a ser buscado:");
            long idProduto = sc.nextLong();

            Optional<Produto> produto = produtoRepository.findById(idProduto);

            if (produto.isPresent()) {
                exibirProduto(produto.get());
            } else {
                System.out.println("Produto não encontrado para o ID " + idProduto);
            }
        } catch (InputMismatchException e) {
            throw new InputMismatchException("Valor informado inválido. Certifique-se de inserir um número válido.");

        }catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Valor de ID informado não pode ser encontrado");
        }
    }

    // Método para exibir produto
    private void exibirProduto(Produto produto) {
        DadosProdutoDto produtoDto = new DadosProdutoDto(
                produto.getIdProduto(),
                produto.getNome(),
                produto.getPreco(),
                produto.getAtivo(),
                produto.getEstoque()
        );

        System.out.println(produtoDto);
    }

    //Método para atualizar as informações de um produto no banco de dados
    private void atualizarProduto(DadosProdutoDto dadosProdutoDto) {

        try{
            System.out.println("Entre com o ID do produto a ser editado/atualiazdo:");
            var idProduto = sc.nextLong();
            sc.nextLine();

            Optional<Produto> produto = produtoRepository.findById(idProduto);

            // Bloco para verificar se o produto esta presente no banco
            if(produto.isPresent()){

                Produto p = produto.get();
                var prod = new DadosProdutoDto(p.getIdProduto(),p.getNome(),p.getPreco(),p.getAtivo(),p.getEstoque());
                System.out.println(prod);

                System.out.println("Digite o novo valor para o nome do Produto ou deixe em branco caso não queira altera-lo: ");
                var nomeProduto = sc.nextLine();

                System.out.println("Digite o novo valor para o preço do Produto ou deixe em branco caso não queira altera-lo: ");
                var precoInput = sc.nextLine();
                var precoProduto = precoInput.isEmpty()? p.getPreco() : Double.parseDouble(precoInput);

                System.out.println("Digite true/false para ativar ou inativar o produto ou deixe em branco caso não queira altera-lo: ");
                var ativoInput = sc.nextLine();
                var ativoProduto = ativoInput.isEmpty() ? p.getAtivo() : Boolean.parseBoolean(ativoInput);

                System.out.println("Digite o novo a quantidade do estoque ou deixe em branco caso não queira altera-lo: ");
                var quantidadeInput = sc.nextLine();
                var quantidadeProduto = quantidadeInput.trim().isEmpty() || Integer.parseInt(quantidadeInput) < 0 ? p.getEstoque() : Integer.parseInt(quantidadeInput);

                DadosProdutoDto dados = new DadosProdutoDto(idProduto,nomeProduto,precoProduto,ativoProduto,quantidadeProduto);

                p.atualizarInformacoes(dados);
                produtoRepository.save(p);

                System.out.println(p);

            }else {
                System.out.println("Não foi possivel atualizar o produto ID não encontrado");
            }
        }catch (RuntimeException e){
            throw new ObjectNotFoundException("Não foi possivel atualizar o produto");
        }
    }

    // Método para inativar o produto
    private void inativarProduto() {
        try{
            System.out.println("Digite o ID do produto a ser inativado: ");
            var idProduto = sc.nextLong();
            Optional<Produto> prod = produtoRepository.findById(idProduto);

            // Bloco para verificar se o produto esta presente no banco
            if(prod.isPresent()){
                Produto p = prod.get();
                p.excluir();

                produtoRepository.save(p);

                System.out.println("Produto inativado com sucesso:" + p);

            }else{
                System.out.println("Não foi possivel inativar o produto");
            }
        }catch (RuntimeException e){
            throw new ObjectNotFoundException("Não foi possivel identificar o produto pelo ID informado");
        }
    }
}
