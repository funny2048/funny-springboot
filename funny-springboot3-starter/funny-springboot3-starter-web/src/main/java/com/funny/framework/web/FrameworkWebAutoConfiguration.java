package com.funny.framework.web;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.funny.framework.web.filter.xss.XssFilter;
import com.funny.framework.web.handler.GlobalExceptionAdvice;

/**
 * @author funny2048
 * @version 1.0
 * @date 2020-07-06
 */
@Configuration
public class FrameworkWebAutoConfiguration implements WebMvcConfigurer {


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //1、先定义一个convert转换消息的对象
        //2、添加fastJson的配置信息，比如:是否要格式化返回json数据；
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 设置fastjson的SerializerFeature序列化属性
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue
        );
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        //3、在convert中添加配置信息
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        // 处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //StringHttpMessageConverter默认编码由ISO-8859-1改为UTF-8
        converters.stream()
                .filter(converter -> converter instanceof StringHttpMessageConverter)
                .forEach(converter -> ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8));
    }

    @Bean(name = "xssFilter")
    @ConditionalOnMissingBean(name = "xssFilter")
    public FilterRegistrationBean xssFilter() {
        XssFilter xssFilter = new XssFilter();
        return new FilterRegistrationBean(xssFilter);
    }

    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnMissingBean(name = "globalExceptionAdvice")
    public GlobalExceptionAdvice globalExceptionAdvice() {
        return new GlobalExceptionAdvice();
    }

}
