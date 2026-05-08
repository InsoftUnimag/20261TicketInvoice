package com.ticketevents.liquidation.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Módulo de Liquidación y Dispersión de Fondos")
                        .description("""
                                API del Módulo 3 - Liquidación y Dispersión de Fondos.
                                
                                Este módulo automatiza la distribución del recaudo entre los 
                                actores involucrados (Promotor, Recinto, Ticketera).
                                
                                ## Modelos de Negocio
                                - **Tarifa Plana**: Monto fijo por el uso del recinto
                                - **Reparto de Ingresos**: Porcentaje sobre la venta bruta
                                
                                ## Matriz de Liquidación
                                | Condición | % Pago Promotor | % Comisión |
                                |-----------|----------------|------------|
                                | Validado  | 90%           | 10%        |
                                | Vendido   | 100%          | 10%        |
                                | Cortesía  | 0%            | Tarifa fija|
                                | Cancelado | -100%         | 0%         |
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("dev@ticketevents.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development"),
                        new Server().url("/").description("Current Server")
                ));
    }
}
