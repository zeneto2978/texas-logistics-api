package com.jose.texaslogistics.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI texasLogisticsOpenApi() {

        Contact contact = new Contact()

                .name("Jose Mota")
                .email("motanetojose@gmail.com");

        Info info = new Info()
            .title("Texas Logistics API")
            .version("1.0.0")
            .description(
                    "REST API for managing drivers, shipments" +
                            " and logistics assignments."
            )

            .contact(contact);

        return new OpenAPI()
                .info(info);
    }
}
