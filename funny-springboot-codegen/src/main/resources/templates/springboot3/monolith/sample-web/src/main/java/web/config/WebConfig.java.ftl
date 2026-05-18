
package ${package}.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: fangli
 * @Date: 2024/12/9
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Resource
//    private TokenInterceptor tokenInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**");
//    }
//
//    @Bean(name = "tokenInterceptor")
//    public TokenInterceptor tokenInterceptor() {
//        return new TokenInterceptor();
//    }
}
