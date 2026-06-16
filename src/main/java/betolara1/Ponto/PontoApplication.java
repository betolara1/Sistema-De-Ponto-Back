package betolara1.Ponto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // <-- Adicionar para ativar o suporte ao cache
public class PontoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PontoApplication.class, args);
	}

}
