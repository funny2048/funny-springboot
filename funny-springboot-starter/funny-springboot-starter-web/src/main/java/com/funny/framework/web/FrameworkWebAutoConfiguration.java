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


    // FastJSON converter 统一在 extendMessageConverters 中注册到列表头部，确保优先于 Jackson

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 将FastJSON converter移到最前面，确保优先于Jackson
        converters.removeIf(converter -> converter instanceof FastJsonHttpMessageConverter);
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue
        );
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        // 把 FastJSON 配置移到 extendMessageConverters 中，用 converters.add(0, fastConverter) 插到列表头部，确保优先于 Jackson
        converters.add(0, fastConverter);

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
