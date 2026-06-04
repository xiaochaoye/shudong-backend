package com.shudong.common.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                .title("树洞后端 API")
                                .description("树洞后端API管理系统")
                                .version("1.0.0")                
                                .contact(new Contact()
                                        .name("Chao")
                                        .url("https://github.com/某某")
                                        .email("xxxxxx@qq.com")))
                                .externalDocs(new ExternalDocumentation()
                                        .description("项目使用文档")
                                        .url("https://github.com/chao/shudong-backend#readme"));
        }
}