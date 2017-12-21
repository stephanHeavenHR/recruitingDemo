package com.havenhr;

import com.havenhr.security.AuthenticationInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static com.google.common.collect.Lists.newArrayList;

@EnableSwagger2
@SpringBootApplication
@EnableAutoConfiguration
public class HavenHRApplication extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(HavenHRApplication.class, args);
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/api/**");
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                                                      .apis(RequestHandlerSelectors.basePackage(getClass().getPackage().getName()))
                                                      .paths(PathSelectors.any())
                                                      .build()
                                                      .globalOperationParameters(newArrayList(new ParameterBuilder().name("X-Auth-Token")
                                                                                                                    .description("Authentication Token")
                                                                                                                    .modelRef(new ModelRef("string"))
                                                                                                                    .parameterType("header")
                                                                                                                    .build()));
    }
}
