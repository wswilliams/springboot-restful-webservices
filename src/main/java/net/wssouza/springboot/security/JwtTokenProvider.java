package net.wssouza.springboot.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import net.wssouza.springboot.exception.CustomException;
import net.wssouza.springboot.entity.Role;

import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

  @Value("${security.jwt.token.secret-key:default-secret-key}")
  private String secretKeyString;

  //@Value("${security.jwt.token.expire-length:default-expire-key}")
  private long validityInMilliseconds = 3600000; // 1h

  @Autowired
  private MyUserDetails myUserDetails;

  // Chave secreta criptograficamente segura
  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    initSecretKey();
  }

  private void initSecretKey() {
    byte[] decodedKey = Base64.getDecoder().decode(secretKeyString);
    secretKey = Keys.hmacShaKeyFor(decodedKey); // Usa uma chave criptograficamente segura
  }

  public String createToken(String username, List<Role> roles) {
    Claims claims = Jwts.claims().setSubject(username);
    claims.put("auth", roles.stream()
            .map(s -> new SimpleGrantedAuthority(s.getAuthority()))
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));

    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(secretKey, SignatureAlgorithm.HS256)  // Usando a chave segura
            .compact();
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
  }

  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      throw new CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}