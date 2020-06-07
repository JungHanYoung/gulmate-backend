package io.hanyoung.gulmatebackend.config;

import io.hanyoung.gulmatebackend.domain.account.Account;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket apiV1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .ignoredParameterTypes(Account.class)
                .groupName("V1")
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.hanyoung.gulmatebackend.web"))
                .paths(PathSelectors.ant("/api/v1/**"))
                .build()
                .apiInfo(new ApiInfo(
                        "Gulmate API",
                        "Family Management App",
                        "1",
                        "www.gulmate.site",
                        new Contact("정한영", "https://github.com/junghanyoung", "8735132@gmail.com"),
                        "Licenses",
                        "www.gulmate.site",
                        new ArrayList<>()
                ));
    }
}
