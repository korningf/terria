package net.terria.registry.terraform;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RegistryConfig implements WebMvcConfigurer {

    @Value("${registry.storage.path:../registry}")
    private String storagePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve provider downloads from registry storage
        registry.addResourceHandler("/storage/providers/**")
                .addResourceLocations("file:" + Paths.get(storagePath, "registry.terraform.io", "providers").toString() + "/");

        // Serve module downloads from registry storage
        registry.addResourceHandler("/storage/modules/**")
                .addResourceLocations("file:" + Paths.get(storagePath, "registry.terraform.io", "modules").toString() + "/");
    }
}