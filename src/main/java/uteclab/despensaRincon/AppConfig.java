package uteclab.despensaRincon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uteclab.despensaRincon.utils.JWTUtil;
import uteclab.despensaRincon.utils.VerificarTokenInterceptor;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    private JWTUtil jwtUtil;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new VerificarTokenInterceptor(jwtUtil));
    }
}