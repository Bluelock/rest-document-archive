package com.bluelock.fileloader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application implements CommandLineRunner {

    private static final Log LOG = LogFactory.getLog(Application.class);

    @Bean
    public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("5120KB");
        factory.setMaxRequestSize("5120KB");
        return factory.createMultipartConfig();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("Application started with arguments:\n");
        if (args == null || args.length == 0) {
            sb.append("\tNONE\n");
        } else {
            for (String argument : args) {
                sb.append("\t").append(argument).append("\n");
            }
        }
        sb.append("\n");
        LOG.info(sb.toString());
    }
}
