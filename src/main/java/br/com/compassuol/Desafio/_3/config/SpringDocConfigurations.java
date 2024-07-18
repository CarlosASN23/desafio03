package br.com.compassuol.Desafio._3.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Ecommerce API")
                        .description("API para simular de forma simples as operações de " +
                                "um ecommerce com os métodos CRUD para produtos e vendas")
                        .contact(new Contact().name("Carlos Alberto Souza Nascimento").url("https://github.com/CarlosASN23"))
                        .version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes("bearer-key",new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP).scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
