package app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 1. O padrão de URL para o qual a regra se aplica
            .allowedOrigins("http://localhost:5500", "http://127.0.0.1:5500") // 2. As URLs do frontend que têm permissão
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 3. Os métodos HTTP permitidos
            .allowedHeaders("*") // 4. Permite todos os cabeçalhos
            .allowCredentials(true); // 5. Permite o envio de credenciais (como cookies)
    }
}
