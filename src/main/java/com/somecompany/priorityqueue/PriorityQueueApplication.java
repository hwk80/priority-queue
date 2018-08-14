package com.somecompany.priorityqueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class PriorityQueueApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PriorityQueueApplication.class, args);
    }
    
    @Bean
    public Docket swaggerEmployeeApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.somecompany.priorityqueue.api"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(
                        new ApiInfoBuilder()
                                .version("1.0")
                                .title("Rubber Duck Order Queue API")
                                .contact(new Contact("Hannes Weitekamp"
                                        , null, "myeaddress@company.com"))
                                .termsOfServiceUrl("Terms of Service go here")
                                .licenseUrl("License goes here")
                                .description("Orderqueue API v1.0").build());
    }
}
