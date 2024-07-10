package br.com.compassuol.Desafio._3.view;

import br.com.compassuol.Desafio._3.dto.DadosItemPedidoDto;
import br.com.compassuol.Desafio._3.dto.DadosProdutoDto;
import br.com.compassuol.Desafio._3.dto.DadosVendaDto;
import br.com.compassuol.Desafio._3.exception.ObjectNotFoundException;
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

    public  void exibirMenu() {
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

                case 5:
                    editarVenda();
                    break;

                case 6:
                    cancelarVenda();
                    break;

                case 0:
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

    // Método para editar uma venda
    private void editarVenda() {

        try{
            System.out.println("Entre com o id da venda:");
            var idVenda = sc.nextLong();

            Optional<Venda> venda = vendaRepository.findById(idVenda);

            if(venda.isPresent()) {
                Venda v = venda.get();
                var dadosVenda = new DadosVendaDto(v.getIdVenda(),v.getDataCriacao(),v.getStatusVenda(),v.getValorVenda());

                // Instanciando o itemPedido
                List<ItemPedido> items = itemPedidoRepository.exibirItensPorVendaId(idVenda);
                ItemPedido itemPedido = items.get(0);

                var dadosItemPedido = new DadosItemPedidoDto(itemPedido.getIdItemPedido(),itemPedido.getVenda(),
                                                             itemPedido.getProduto(), itemPedido.getPrecoDoItem(),
                                                             itemPedido.getQuantidadeDoItem(), itemPedido.getDataItemPedido());
                System.out.println(dadosItemPedido);

                // Instanciando o produto
                Optional<Produto> produto = produtoRepository.findById(itemPedido.getProduto().getIdProduto());
                Produto p = produto.get();
                var produtos = new DadosProdutoDto(p.getIdProduto(),p.getNome(),p.getPreco(),p.getAtivo(),p.getEstoque());

                // Atualização do Status do Pedido
                System.out.println("Para alterar o Status da venda digite 1 - Pendente | 2 - Efetivada | 3 - Cancelada ou deixe em branco caso não queira altera-lo:");
                var novoStatusVenda = sc.nextInt();
                switch (novoStatusVenda) {
                    case 1:
                        v.setStatusVenda(StatusVenda.PENDENTE);
                        break;
                    case 2:
                        v.setStatusVenda(StatusVenda.EFETIVADA);
                        break;
                    case 3:
                        v.setStatusVenda(StatusVenda.CANCELADA);
                        break;
                    default:
                        System.out.println("Opção inválida");
                }
                // Bloco para verificar se o Status do pedido foi alterado para cancelado
                if(v.getStatusVenda() == StatusVenda.CANCELADA){

                    v.setValorVenda(0.0);
                    v.setStatusVenda(StatusVenda.CANCELADA);
                    DadosVendaDto dados = new DadosVendaDto(v.getIdVenda(),v.getDataCriacao(),v.getStatusVenda(),v.getValorVenda());

                    v.atualizarInformacoes(dados);
                    vendaRepository.save(v);

                    // Bloco para atualizar o estoque do produto após cancelamento da venda
                    var novoEstoque = produtos.estoque() + itemPedido.getQuantidadeDoItem();
                    produtos = new DadosProdutoDto(p.getIdProduto(),p.getNome(),p.getPreco(),p.getAtivo(),novoEstoque);
                    p.atualizarInformacoes(produtos);
                    produtoRepository.save(p);

                    // Zerar a quantidade comprada do produto e o valor
                    itemPedido.setQuantidadeDoItem(0);
                    itemPedido.setPrecoDoItem(0.0);
                    DadosItemPedidoDto dadosItem = new DadosItemPedidoDto(itemPedido.getIdItemPedido(),itemPedido.getVenda(),
                                                                          itemPedido.getProduto(), itemPedido.getPrecoDoItem(),
                                                                          itemPedido.getQuantidadeDoItem(),itemPedido.getDataItemPedido());

                    itemPedido.atualizarInformacoes(dadosItem);
                    itemPedidoRepository.save(itemPedido);
                    System.out.println("Venda atualizada com sucesso");

                } else if (v.getStatusVenda() == StatusVenda.EFETIVADA || v.getStatusVenda() == StatusVenda.PENDENTE) {

                    System.out.println("Digite a nova quantidade de compra do produto ou deixe em branco caso não queira alterá-lo: ");
                    var quantidadeInput = sc.nextLine(); // Consumir a quebra de linha pendente
                    quantidadeInput = sc.nextLine(); // Ler a entrada do usuário corretamente

                    var quantidadeVenda = quantidadeInput.trim().isEmpty() || Integer.parseInt(quantidadeInput) < 0 ? itemPedido.getQuantidadeDoItem() : Integer.parseInt(quantidadeInput);

                    if(quantidadeVenda != 0 && quantidadeVenda > 0 && quantidadeVenda <= p.getEstoque()){

                        // Voltar o estoque para não haver duas saidas do estoque do produto com a venda anterior
                        var novoEstoque = produtos.estoque() + itemPedido.getQuantidadeDoItem();
                        produtos = new DadosProdutoDto(p.getIdProduto(),p.getNome(),p.getPreco(),p.getAtivo(),novoEstoque);
                        p.atualizarInformacoes(produtos);
                        produtoRepository.save(p);

                        var valorTotal = itemPedido.getProduto().getPreco() * quantidadeVenda;

                        DadosVendaDto dados = new DadosVendaDto(v.getIdVenda(), v.getDataCriacao(), v.getStatusVenda(), valorTotal);

                        DadosItemPedidoDto dadosItemPedidoDto = new DadosItemPedidoDto(itemPedido.getIdItemPedido(), itemPedido.getVenda(),
                                itemPedido.getProduto(), valorTotal,
                                quantidadeVenda, itemPedido.getDataItemPedido());

                        // Atualizar o estoque do produto após atualização da venda
                        var atualizacao = produtos.estoque() - quantidadeVenda;
                        produtos = new DadosProdutoDto(p.getIdProduto(), p.getNome(), p.getPreco(), p.getAtivo(), atualizacao);
                        p.atualizarInformacoes(produtos);
                        produtoRepository.save(p);

                        // Atualizar informações da venda e do item de pedido
                        v.atualizarInformacoes(dados);
                        itemPedido.atualizarInformacoes(dadosItemPedidoDto);

                        vendaRepository.save(v);
                        itemPedidoRepository.save(itemPedido);
                        System.out.println("Venda atualizada com sucesso");

                    }else{
                        System.out.println("Não foi possivel fazer a atualização da venda");
                    }
            }
            else {
                    System.out.println("Não foi possivel alterar a venda");
                }
            }

        }catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Não foi possivel atualizar a venda");
        }
    }

    // Método para cancelar a venda
    private void cancelarVenda() {

        System.out.println("Entre com o id da venda:");
        var idVenda = sc.nextLong();

        Optional<Venda> venda = vendaRepository.findById(idVenda);

        if (venda.isPresent()) {
            Venda v = venda.get();
            var dadosVenda = new DadosVendaDto(v.getIdVenda(), v.getDataCriacao(), v.getStatusVenda(), v.getValorVenda());

            // Instanciando o itemPedido
            List<ItemPedido> items = itemPedidoRepository.exibirItensPorVendaId(idVenda);
            ItemPedido itemPedido = items.get(0);

            var dadosItemPedido = new DadosItemPedidoDto(itemPedido.getIdItemPedido(), itemPedido.getVenda(),
                    itemPedido.getProduto(), itemPedido.getPrecoDoItem(),
                    itemPedido.getQuantidadeDoItem(), itemPedido.getDataItemPedido());
            System.out.println(dadosItemPedido);

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
            System.out.println("Venda cancelada com sucesso");
        }
    }

}
