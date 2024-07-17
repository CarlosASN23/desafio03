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

@SpringBootApplication
public class Desafio03Application1 {

	public static void main(String[] args) {
		SpringApplication.run(Desafio03Application1.class, args);
	}

}
