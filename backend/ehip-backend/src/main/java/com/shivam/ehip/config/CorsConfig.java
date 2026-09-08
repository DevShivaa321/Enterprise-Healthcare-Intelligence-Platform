package com.shivam.ehip.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;      /** define CORS rules **/
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;  /** lets you customize Spring MVC **/

@Configuration                                   /** tells spring that this class contains configuration settings  **/
public class CorsConfig implements WebMvcConfigurer{

    @Override                                  /** Spring calls this method during startup **/
    public void addCorsMappings(CorsRegistry registry) {           /** registry is used to define CORS RULE **/

        registry.addMapping("/**")                                 /** Apply CORS to ALL endpoints **/
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")    /** OPTIONS -- Preflight Req. in HTTP , HEAD -- metadata **/
                .allowedHeaders("*");                              /** ALLOW ALL HEADERS **/

    }
}
