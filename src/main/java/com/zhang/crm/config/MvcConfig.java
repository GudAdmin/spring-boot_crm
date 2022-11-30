package com.zhang.crm.config;
import com.zhang.crm.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author zb
 * @version 1.0
 * @date 2022/11/29 10:25
 * @ClassName MvcConfig
 * @Description TODD
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter{
    @Bean
    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //需要一个实现HandlerInterceptor接口的拦截器实例，这里使用的是NoLoginInterceptor
        registry.addInterceptor(noLoginInterceptor())
        //用于设置拦截器过滤器路径规则
                .addPathPatterns("/**")
        //用于设置不需要拦截的过滤器规则
                .excludePathPatterns("/index","/user/login","/css/**","images/**","/js/**","/lib/**");
    }
}
