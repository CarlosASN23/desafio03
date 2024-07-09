package br.com.compassuol.Desafio._3;

import br.com.compassuol.Desafio._3.repository.ProdutoRepository;
import br.com.compassuol.Desafio._3.view.ProdutoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Desafio03Application implements CommandLineRunner {

	@Autowired
	private ProdutoRepository produtoRepository;

	public static void main(String[] args) {
		SpringApplication.run(Desafio03Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ProdutoView produtoView = new ProdutoView(produtoRepository);
		produtoView.exibirMenu();
	}
}
