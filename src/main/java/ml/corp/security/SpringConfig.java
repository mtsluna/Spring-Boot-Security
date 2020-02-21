package ml.corp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ml.corp.security.auth.JWTAuthorizationFilter;

@Configuration
public class SpringConfig {

	@Bean
	public JWTAuthorizationFilter jwtAuthorizationFilter() {
		return new JWTAuthorizationFilter();
	}
	
}
