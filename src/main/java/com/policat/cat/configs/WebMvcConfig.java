package com.policat.cat.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/about").setViewName("about-page");
        registry.addViewController("/admin").setViewName("admin_page");
        registry.addViewController("/addQ").setViewName("admin-addQD");
        registry.addViewController("/changePass").setViewName("admin-changePass");
        registry.addViewController("/addAdm").setViewName("admin-add");
        registry.addViewController("/contact").setViewName("contact");
        registry.addViewController("/tutorials").setViewName("tutorials");






    }
}
