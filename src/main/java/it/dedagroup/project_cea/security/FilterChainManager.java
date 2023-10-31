package it.dedagroup.project_cea.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import it.dedagroup.project_cea.model.Role;

@Configuration
public class FilterChainManager {

	@Autowired
	private JwtFilter filter;
	@Autowired
	private AuthenticationProvider provider;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf(csrf->csrf.disable())
			.authorizeHttpRequests(auth->auth
					.requestMatchers("/all/**").permitAll()
					.requestMatchers("/administrator/**").hasRole(Role.ADMINISTRATOR.toString())
					.requestMatchers("/technician/**").hasRole(Role.TECHNICIAN.toString())
					.requestMatchers("/customer/**").hasRole(Role.CUSTOMER.toString())
					.requestMatchers("/secretary/**").hasRole(Role.SECRETARY.toString())
					.anyRequest().authenticated()
				).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
				.authenticationProvider(provider)
				.sessionManagement(s->
				s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}
}
