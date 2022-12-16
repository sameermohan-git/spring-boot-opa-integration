package com.smohan.opaauthorization.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.smohan.opaauthorization.opa.OPADecisionManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

	@Value("${opa.auth.url}")
	private String opaAuthURL;

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
    	List<UserDetails> users = new ArrayList<>();
        UserDetails admin = User.withDefaultPasswordEncoder()
            .username("sameer")
            .password("password")
            .roles("ADMIN")
            .build();
        users.add(admin);
        
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("John")
                .password("password")
                .roles("USER")
                .build();
            users.add(user);
        return new InMemoryUserDetailsManager(users);
    }


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.cors().and().csrf().disable()
		.authorizeHttpRequests((authorize) -> authorize
				.anyRequest()//.requestMatchers("/my/authorized/endpoint")
	            .access(new OPADecisionManager(opaAuthURL))
	        )
		.httpBasic(Customizer.withDefaults());

		return http.build();
	}

}
