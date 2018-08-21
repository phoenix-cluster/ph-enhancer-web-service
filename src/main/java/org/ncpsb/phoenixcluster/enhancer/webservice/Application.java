package org.ncpsb.phoenixcluster.enhancer.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/*
 * This is the main Spring Boot application class. It configures Spring Boot, JPA, Swagger
 */

@SpringBootApplication
@Configuration
@EnableAutoConfiguration  // Sprint Boot Auto Configuration
@EnableScheduling
@ComponentScan(basePackages = "org.ncpsb.phoenixcluster.enhancer.webservice")
    public class Application extends SpringBootServletInitializer {

    private static final Class<Application> applicationClass = Application.class;
    private static final Logger log = LoggerFactory.getLogger(applicationClass);

	public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "true");
		SpringApplication.run(applicationClass, args);
	}

	    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }

}


