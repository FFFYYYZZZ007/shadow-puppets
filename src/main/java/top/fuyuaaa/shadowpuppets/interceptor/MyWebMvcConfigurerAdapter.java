package top.fuyuaaa.shadowpuppets.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-05 21:36
 */
@Configuration
public class MyWebMvcConfigurerAdapter implements WebMvcConfigurer {

    @Bean
    public LoginInterceptor myAuthInterceptor() {
        return new LoginInterceptor();
    }

    public OncePerRequestContextClearInterceptor oncePerRequestContextClearInterceptor(){
        return new OncePerRequestContextClearInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myAuthInterceptor());
        registry.addInterceptor(oncePerRequestContextClearInterceptor());
    }
}
