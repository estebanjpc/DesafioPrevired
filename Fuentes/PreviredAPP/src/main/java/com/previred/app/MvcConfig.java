package com.previred.app;

import org.springframework.context.annotation.Bean;

// import java.nio.file.Paths;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	
	@Bean("restTemplate")
	public RestTemplate registrarRestTemplate() {
		return new RestTemplate();
	}

}
