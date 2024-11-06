package com.reservationbroker.reservation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration to map /images/** to the uploaded_images directory
 */
@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Expose the uploaded images directory under /images/**
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:./uploaded_images/");
    }
}
