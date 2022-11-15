
package com.ssafy.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.*;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//http://localhost:9999/vuews/swagger-ui.html

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket postsApi() {
	    return new Docket(DocumentationType.SWAGGER_2)
	            .groupName("ssafyHappyHouse")
	            .apiInfo(apiInfo())
	            .select()
	            .apis((Predicate<RequestHandler>) RequestHandlerSelectors.basePackage("com.ssafy.hw"))
	            .paths((Predicate<String>) PathSelectors.any())
	            .build();
	}

	private ApiInfo apiInfo() {
	    return new ApiInfoBuilder().title("SSAFY API")
	            .description("<h2>SSAFY Happy House API Reference for Developers</h2><img src=\"\\assets\\img\\logo.jpg\">")
	            .termsOfServiceUrl("https://edu.ssafy.com")
	            .license("SSAFY License")
	            .licenseUrl("https://www.ssafy.com/ksp/jsp/swp/etc/swpPrivacy.jsp").version("1.0").build();
	}
}