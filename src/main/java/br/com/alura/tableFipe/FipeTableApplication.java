package br.com.alura.tableFipe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import br.com.alura.tableFipe.Principal.Principal;

@SpringBootApplication(scanBasePackages = "br.com.alura.tableFipe")
public class FipeTableApplication implements CommandLineRunner {

	private final Principal principal;

	// Injeção de dependência da classe Principal
	public FipeTableApplication(Principal principal) {
		this.principal = principal;
	}

	public static void main(String[] args) {
		SpringApplication.run(FipeTableApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.exibeMenu();
	}
}
