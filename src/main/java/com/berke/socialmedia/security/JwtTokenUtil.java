package com.berke.socialmedia.security;

import com.berke.socialmedia.dao.RoleRepository;
import com.berke.socialmedia.dao.entity.Role;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.berke.socialmedia.util.Constants.*;

@Component
public class JwtTokenUtil {

    private final RoleRepository roleRepository;

    public JwtTokenUtil(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token){
        return getExpirationDateFromToken(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();

        final List<Role> roles = userDetails.getAuthorities().stream().filter(authority -> authority.getAuthority()
                .startsWith("ROLE_")).map(authority -> roleRepository.findByName(authority.getAuthority())).collect(Collectors.toList());

        final List<String> scopes = roles.stream().map(role -> role.getName()).collect(Collectors.toList());
        claims.put(AUTHORITIES_KEY, scopes);

        final List<JwtAuthorityEntity> authorities = roles.stream().map(role -> new JwtAuthorityEntity(role.getName(),
                role.getPrivileges().stream().map(privilege -> privilege.getName()).collect(Collectors.toList())))
                .collect(Collectors.toList());
        claims.put("authorities", authorities);


        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject){
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setHeaderParam("typ","JWT")
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public UsernamePasswordAuthenticationToken getAuthentication(final String token,
                                                                 UserDetails userDetails){

        final JwtParser jwtParser = Jwts.parser().setSigningKey(SIGNING_KEY);

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();

        final List<String> roles = (List<String>) claims.get(AUTHORITIES_KEY);
        final List<Map<String,Object>> authorityEntities = (List<Map<String, Object>>) claims.get("authorities");
        final List<String> addedAuthorities = new ArrayList<>();
        final Collection<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        for(Map<String, Object> authority : authorityEntities){
            ((List<String>)authority.get("privileges")).stream().filter(privilege -> !addedAuthorities.contains(privilege))
                    .forEach(privilege -> {
                        authorities.add(new SimpleGrantedAuthority(privilege));
                        addedAuthorities.add(privilege);
                    });
        }
        return new UsernamePasswordAuthenticationToken(userDetails,"", authorities);
    }
}
