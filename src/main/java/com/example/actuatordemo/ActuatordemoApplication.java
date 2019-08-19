package com.example.actuatordemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class ActuatordemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActuatordemoApplication.class, args);
	}

	@Configuration
	public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication()
					.withUser("user")
					.password("{noop}password")
					.roles("ACTUATOR");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.httpBasic();
			http.authorizeRequests()
					.requestMatchers(EndpointRequest.to("health", "info")).permitAll()
					.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
					//.antMatchers("/actuator/jolokia/**").hasRole("ACTUATOR") // works
					.anyRequest().denyAll();
		}
	}

	// curl -v -u "user:password" http://localhost:8080/actuator/jolokia/list

}
