package ru.rsl.api.web;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI rslOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Russian Sign Language API")
                        .description("Темы → уроки → жесты. Заголовок X-User-Id после POST /users.")
                        .version("0.1.0"))
                .components(new Components().addSecuritySchemes(
                        "userId",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(CurrentUser.HEADER)
                ));
    }
}
