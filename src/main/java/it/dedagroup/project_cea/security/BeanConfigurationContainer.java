package it.dedagroup.project_cea.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.dedagroup.project_cea.exception.model.UserNotFoundException;
import it.dedagroup.project_cea.repository.CustomerRepository;

@Configuration
public class BeanConfigurationContainer {

	@Autowired
	private CustomerRepository repo;
	
	@Bean
	public UserDetailsService userDetailsServiceCustomer() {
		return u->repo.findCustomerByUsername(u)
				.orElseThrow(()->new UserNotFoundException("Customer with username "+u+" not found!"));
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider dap=new DaoAuthenticationProvider();
		dap.setPasswordEncoder(passwordEncoder());
		dap.setUserDetailsService(userDetailsServiceCustomer());
		return dap;
	}
}
