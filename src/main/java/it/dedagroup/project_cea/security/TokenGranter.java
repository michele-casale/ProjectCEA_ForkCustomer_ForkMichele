package it.dedagroup.project_cea.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.dedagroup.project_cea.exception.model.UserNotFoundException;
import it.dedagroup.project_cea.model.Customer;
import it.dedagroup.project_cea.model.Role;
import it.dedagroup.project_cea.repository.CustomerRepository;

@Service
public class TokenGranter {

	@Autowired
	private CustomerRepository repo;
	
	@Value("${it.dedagroup.jwt.key}")
	private String securityKey;
	
	private SecretKey getKey() {
		return Keys.hmacShaKeyFor(securityKey.getBytes());
	}
	
	public String generateToken(Customer c) {
		long expirations=1000L*60*60*24*15;
		return Jwts.builder()
				.claims()
					.add("role",c.getRole())
					.subject(c.getUsername())
					.issuedAt(new Date(System.currentTimeMillis()))
					.expiration(new Date(System.currentTimeMillis()+expirations))
				.and()
					.signWith(getKey())
				.compact();
	}
	
	private Claims getClaims(String token) {
		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	private <T> T getValue(Function<Claims,T> function, String token) {
		return function.apply(getClaims(token));
	}
	
	public String getUsername(String token) {
		return getValue(c->c.getSubject(), token);
	}
	
	public LocalDateTime getIssuedAt(String token) {
		return getValue(Claims::getIssuedAt, token)
				.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}
	
	public LocalDateTime getExpirationTime(String token) {
		return getValue(Claims::getExpiration, token)
				.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}
	
	public Role getRole(String token) {
		return getClaims(token).get("role", Role.class);
	}
	
	public Customer getCustomer(String token) {
		return repo.findCustomerByUsername(getUsername(token))
				.orElseThrow(()->new UserNotFoundException(token));
	}
}
