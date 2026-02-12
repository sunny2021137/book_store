package com.example.springboot.common;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// 跨域配置
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 1. 設置允許訪問的源地址（例如：http://localhost:8080）
        corsConfiguration.addAllowedOrigin("*");

        // 2. 設置允許訪問的請求頭
        corsConfiguration.addAllowedHeader("*");

        // 3. 設置允許訪問的請求方法（GET, POST, PUT, DELETE 等）
        corsConfiguration.addAllowedMethod("*");

        // 4. 對所有接口配置跨域設置
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
}