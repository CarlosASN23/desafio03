package br.com.compassuol.Desafio._3.view;

import br.com.compassuol.Desafio._3.dto.DadosProdutoDto;
import br.com.compassuol.Desafio._3.dto.DadosVendaDto;
import br.com.compassuol.Desafio._3.model.ItemPedido;
import br.com.compassuol.Desafio._3.model.Produto;
import br.com.compassuol.Desafio._3.model.Venda;
import br.com.compassuol.Desafio._3.model.enums.StatusVenda;
import br.com.compassuol.Desafio._3.repository.ItemPedidoRepository;
import br.com.compassuol.Desafio._3.repository.ProdutoRepository;
import br.com.compassuol.Desafio._3.repository.VendaRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VendaView {

    Scanner sc = new Scanner(System.in);

    // Repositories
    private VendaRepository vendaRepository;
    private ProdutoRepository produtoRepository;
    private ItemPedidoRepository itemPedidoRepository;

    // DTOs
    private List<DadosVendaDto> dadosVenda = new ArrayList<>();
    private DadosVendaDto dados;

    //Model
    private List<Venda> vendas =new ArrayList<>();
    private Venda venda;


    public VendaView(VendaRepository vendaRepository, ProdutoRepository produtoRepository, ItemPedidoRepository itemPedidoRepository){
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public void exibirMenu() {
        var opcao = -1;

        while (opcao != 0) {
            var menu = """
                \n======== MENU OPÇÕES ========
             
                1 - Cadastrar Venda
                2 - Buscar venda por ID
                3 - Filtrar venda por semana
                4 - Filtrar venda por Mês
                5 - Editar venda
                6 - Cancelar venda
                
                
                0 - Sair
                ==============================
                """;
            System.out.println(menu);
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {

                case 1:
                    cadastrarVenda();
                    break;

                case 2:
                    buscarVendaPorId();
                    break;

                case 3:
                    filtrarVendaPorSemana();;
                    break;

                case 4:
                    filtrarVendaPorMes();
                    break;

//                case 5:
//                    editarVenda();
//                    break;
//
//                case 6:
//                    cancelarVenda();
//                    break;

                case 0:
                    System.out.println("Encerrando aplicação....");
                    break;

                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    // Método para gerar uma nova venda
    private void cadastrarVenda() {

        ItemPedido item = new ItemPedido();

        while (true) {

            // Listar produtos ativos e disponiveis para venda
            List<Produto> prod;
            prod = produtoRepository.findAllByAtivoTrue();
            System.out.println(prod);

            System.out.println("Digite o ID do produto para ser alocado à venda (ou -1 para sair): ");
            var idProduto = sc.nextLong();
            sc.nextLine();

            Optional<Produto> produto = produtoRepository.findById(idProduto);

            if (idProduto == -1) {
                break; // Sai do loop se o usuário digitar -1
            }

            // Verificar se o produto esta presente na lista
            if (produto.isPresent()) {
                Produto p = produto.get();
                var produtos = new DadosProdutoDto(p.getIdProduto(),p.getNome(),p.getPreco(),p.getAtivo(),p.getEstoque());

                // Verificar se o produto esta ativo e com uma quantidade em estoque é maior que zero
                if (p.getAtivo() == true && p.getEstoque() > 0){

                    Venda venda = new Venda();
                    venda.setStatusVenda(StatusVenda.EFETIVADA);
                    venda.setDataCriacao(ZonedDateTime.now());

                    System.out.println("Digite a quantidade do produto a ser vendida:");
                    item.setQuantidadeDoItem(sc.nextInt());

                    // Verificar se quantidadeVendida é maior que a quantidade em estoque e se quantidadeVendida > 0
                    if(produtos.estoque() < item.getQuantidadeDoItem() && item.getQuantidadeDoItem()  != 0 && item.getQuantidadeDoItem()  > 0){

                        /* Se a quantidade vendida for maior que a quantidade em estoque
                        atribui a quantidade vendida o valor da quantidade maxima em estoque
                        zerando o estoque do produto. */
                        item.setQuantidadeDoItem(produtos.estoque());

                        item.setPrecoDoItem(produtos.preco());

                        var valorTotal = item.getPrecoDoItem() * item.getQuantidadeDoItem();

                         item.setDataItemPedido(ZonedDateTime.now());

                        venda.setValorVenda(valorTotal);
                        vendaRepository.save(venda);

                        var itemPedido = new ItemPedido(venda,p,valorTotal, item.getQuantidadeDoItem());

                        itemPedidoRepository.save(itemPedido);
                        System.out.println(itemPedido);

                        // Bloco para atualizar o estoque do produto após a venda
                        var novoEstoque = produtos.estoque() - item.getQuantidadeDoItem();
                        produtos= new DadosProdutoDto(p.getIdProduto(),p.getNome(),p.getPreco(),p.getAtivo(),novoEstoque);
                        p.atualizarInformacoes(produtos);
                        produtoRepository.save(p);

                        // Verificar se a quantidade de estoque do produto é maior que quantidade vendida
                        // e que esta é maior que zero
                    }else if (produtos.estoque() > item.getQuantidadeDoItem() && item.getQuantidadeDoItem() >0){

                        item.setPrecoDoItem(produtos.preco());

                        var valorTotal = item.getPrecoDoItem() * item.getQuantidadeDoItem();

                        venda.setValorVenda(valorTotal);
                        vendaRepository.save(venda);

                        var itemPedido = new ItemPedido(venda,p,valorTotal, item.getQuantidadeDoItem());

                        itemPedidoRepository.save(itemPedido);
                        System.out.println(itemPedido);

                        // Bloco para atualizar o estoque do produto após a venda
                        var novoEstoque = produtos.estoque() - item.getQuantidadeDoItem();
                        produtos = new DadosProdutoDto(p.getIdProduto(),p.getNome(),p.getPreco(),p.getAtivo(),novoEstoque);
                        p.atualizarInformacoes(produtos);
                        produtoRepository.save(p);

                    }else{
                        System.out.println("Não foi possivel realizar a venda");
                    }
                }else{
                    System.out.println("A venda deve ser de um produto ativo e/ou com uma quantidade de estoque > 0");
                }
            } else {
                System.out.println("Produto não encontrado. Tente novamente.");
            }
        }
    }

    // Método para buscar venda por id
    private void buscarVendaPorId() {
        try{
            System.out.println("Entre com o id da venda:");
            var idVenda = sc.nextLong();

            Optional<Venda> venda = vendaRepository.findById(idVenda);
            if(venda.isPresent()) {

                List<ItemPedido> items = itemPedidoRepository.exibirItensPorVendaId(idVenda);
                for(ItemPedido item : items){
                    System.out.println(item);
                }
            }
        }catch (InputMismatchException e ){
            throw new InputMismatchException("Valor informado inválido. Certifique-se de inserir um número válido.");
        }
    }

    // Método para Filtrar vendas por semana
    private void filtrarVendaPorSemana() {

        System.out.println("Digite a data da semana inicial (no formato yyyy-MM-dd):");
        String semanaInicialStr = sc.nextLine();
        var inicio = conversaoData(semanaInicialStr);

        System.out.println("Digite a data da semana final (no formato yyyy-MM-dd):");
        String semanaFinalStr = sc.nextLine();
        var fim = conversaoData(semanaFinalStr);

        List<ItemPedido> vendasSemana = itemPedidoRepository.findByDataItemPedidoBetween(inicio, fim);

        // Exibe as vendas da semana
        for (ItemPedido item : vendasSemana) {
            System.out.println(item);
        }
    }

    // Método para converter uma String no formato de data correto
    private ZonedDateTime conversaoData(String dataUsuario) {

        String dataStr = dataUsuario;

        // Converte a string para um objeto Date
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data;
        try {
            data = inputFormat.parse(dataStr);
        } catch (ParseException e) {
            System.err.println("Erro ao converter a data. Certifique-se de usar o formato correto.");
            return null;
        }

        // Agora, formate a data no formato desejado (2024-07-11T00:23:28.213917Z)
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
        String dataFormatada = outputFormat.format(data);

        return ZonedDateTime.parse(dataFormatada);
    }

    // Método para filtrar por mês
    private void filtrarVendaPorMes() {

        System.out.println("Digite o mês (no formato yyyy-MM):");
        String mesStr = sc.nextLine();

        var inicioMes = conversaoData(mesStr + "-01"); // Primeiro dia do mês
        var fimMes = conversaoData(mesStr + "-31"); // Último dia do mês

        List<ItemPedido> vendasMes = itemPedidoRepository.findByDataItemPedidoBetween(inicioMes, fimMes);

        if(!vendasMes.isEmpty()){
            // Exibe as vendas do mês
            for (ItemPedido item : vendasMes) {
                System.out.println(item);
            }
        }else{
            YearMonth yearMonth = YearMonth.parse(mesStr, DateTimeFormatter.ofPattern("yyyy-MM"));
            var mesAnoFormatado = yearMonth.format(DateTimeFormatter.ofPattern("MMMM/yyyy",Locale.ENGLISH));
            System.out.println("Não houveram vends durante o mês de: " + mesAnoFormatado);
        }

    }



}
