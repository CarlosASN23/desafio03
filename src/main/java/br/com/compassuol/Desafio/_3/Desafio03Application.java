package br.com.compassuol.Desafio._3;

import br.com.compassuol.Desafio._3.repository.ItemPedidoRepository;
import br.com.compassuol.Desafio._3.repository.ProdutoRepository;
import br.com.compassuol.Desafio._3.repository.VendaRepository;
import br.com.compassuol.Desafio._3.view.ProdutoView;
import br.com.compassuol.Desafio._3.view.VendaView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;
//
//@SpringBootApplication
//public class Desafio03Application implements CommandLineRunner {
//
//	@Autowired
//	private ProdutoRepository produtoRepository;
//	@Autowired
//	private VendaRepository vendaRepository;
//
//	@Autowired
//	private ItemPedidoRepository itemPedidoRepository;
//
//	public static void main(String[] args) {
//		SpringApplication.run(Desafio03Application.class, args);
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//
//		Scanner sc = new Scanner(System.in);
//		ProdutoView produtoView = new ProdutoView(produtoRepository);
//		VendaView vendaView = new VendaView(vendaRepository,produtoRepository,itemPedidoRepository);
//
//		var opcao = -1;
//
//		while(opcao != 0){
//
//			var menu = """
//                \n======== MENU OPÇÕES ========
//
//                1 - Produto
//                2 - Venda
//
//                0 - Sair
//                ===============================
//                """;
//
//			System.out.println(menu);
//			opcao = sc.nextInt();
//			sc.nextLine();
//
//			switch (opcao){
//				case 1:
//					produtoView.exibirMenu();
//					break;
//
//				case 2:
//					vendaView.exibirMenu();
//					break;
//
//				case 0:
//					System.out.println("Encerrando a aplicação");
//					break;
//
//				default:
//					System.out.println("Digite uma opção válida");
//			}
//		}
//	}
//}
