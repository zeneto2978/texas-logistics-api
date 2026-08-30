package com.jose.texaslogistics.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public OpenAPI texasLogisticsOpenApi() {

        Contact contact = new Contact();

        contact.setName("Jose Mota");
        contact.setEmail("motanetojose@gmail.com");

        Info info = new Info()
            .title("Texas Logistics API")
            .version("1.0.0")
            .description(
                    "REST API for managing drivers, shipments" +
                            "and logistics assignments."
            )

            .contact(contact);

        return new OpenAPI()
                .info(info);
    }
}
