package ua.org.blablacar.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PaginationConfig {

    @Bean
    PageableHandlerMethodArgumentResolverCustomizer paginationCustomizer() {
        return r -> {
            r.setPageParameterName("pageNumber");
            r.setSizeParameterName("pageSize");
            r.setOneIndexedParameters(true);
        };
    }
}