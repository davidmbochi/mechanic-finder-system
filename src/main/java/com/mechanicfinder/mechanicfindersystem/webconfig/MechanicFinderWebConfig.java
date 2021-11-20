package com.mechanicfinder.mechanicfindersystem.webconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MechanicFinderWebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String mechanicProfileImagePath = getUploadPath("./mechanic-images");

        registry.addResourceHandler("/mechanic-images/**")
                .addResourceLocations("file:/"+mechanicProfileImagePath+"/");

        String qualificationDocumentPath = getUploadPath("./mechanic-qualifications");

        registry.addResourceHandler("/mechanic-qualifications/**")
                .addResourceLocations("file:/"+qualificationDocumentPath+"/");

        String customerProfileImagePath = getUploadPath("./customer-images");

        registry.addResourceHandler("/customer-images/**")
                .addResourceLocations("file:/"+customerProfileImagePath+"/");


    }

    private String getUploadPath(String directory) {
        Path uploadDirPath = Paths.get(directory);
        return uploadDirPath.toFile().getAbsolutePath();
    }
}
