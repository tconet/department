package com.cesar.sharing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${application.description}") String appDesciption,
            @Value("${application.version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                    .title("Sample Application API")
                    .version(appVersion)
                    .description(appDesciption)
                    .contact(new Contact().name("Support").email("support@company.org.br").url("www.company.org.br"))
                    .termsOfService("http://swagger.io/terms/")
                    .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

}
