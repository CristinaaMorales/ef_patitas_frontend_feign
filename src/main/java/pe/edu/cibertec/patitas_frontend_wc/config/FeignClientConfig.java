package pe.edu.cibertec.patitas_frontend_wc.config;

import feign.Logger;
import feign.Request;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients

public class FeignClientConfig {

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(3000,3000);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}