package com.br.kesyo.carmoloc_api.configs;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carmoLocOpenAPI() {
        return new OpenAPI()
            .info(new io.swagger.v3.oas.models.info.Info()
                .title("CarmoLoc API")
                .description("API de locação de máquinas e equipamentos para construção civil")
                .version("1.0.0"));
    }
}
