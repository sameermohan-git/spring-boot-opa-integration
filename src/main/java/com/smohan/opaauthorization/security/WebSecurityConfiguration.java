package com.smohan.opaauthorization.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.smohan.opaauthorization.opa.OPADecisionManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

	@Value("${opa.auth.url}")
	private String opaAuthURL;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("john123").password(passwordEncoder().encode("password")).roles("USER")
		.and()
		.withUser("admin123").password(passwordEncoder().encode("admin")).roles("ADMIN");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.cors().and().csrf().disable()
		.authorizeHttpRequests((authorize) -> authorize
				.anyRequest()//.requestMatchers("/my/authorized/endpoint")
	            .access(new OPADecisionManager())
	            .anyRequest()
	            .authenticated()
	        )
		.httpBasic(Customizer.withDefaults());

		return http.build();
	}

}
