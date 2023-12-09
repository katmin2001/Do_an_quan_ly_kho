package com.kenzy.manage.do_an_quan_ly_kho.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {
    public void addResourceHandlers(final ResourceHandlerRegistry registry){
        registry.addResourceHandler("/product-images/**").addResourceLocations("classpath:/product-images/");
        registry.addResourceHandler("/avatar-image/**").addResourceLocations("classpath:/avatar-image/");
        registry.addResourceHandler("/category-image/**").addResourceLocations("classpath:/category-image/");
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + "upload/");
    }
}
