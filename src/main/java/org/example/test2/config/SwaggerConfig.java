package org.example.test2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
@Configuration
@Profile({"dev", "test"})
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30) // 使用 OpenAPI 3.0 标准
                .apiInfo(apiInfo())
                .select()
                // ⚠️ 这里极其重要：必须改成你自己的 Controller 所在的包路径！
                .apis(RequestHandlerSelectors.basePackage("org.example.test1.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("电商后台管理系统 API 文档")
                .description("实习生首日通关项目接口测试文档")
                .contact(new Contact("Zoukun", "http://yoursite.com", "2788784114@QQ.com"))
                .version("1.0")
                .build();
    }
}