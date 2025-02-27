package Review.auth.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "KjFhT3JXc2pYNkpQRVZ2QlFid0Z5anNTV2d3NUZHQmQ=";


    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }


    public String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String getUsernameFromToken(String token) {
        try {
            return getClaim(token, Claims::getSubject);
        }catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }catch (Exception e) {
            return false;
        }
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        }catch (Exception e) {
            return null;
        }
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        try {
            final Claims claims = getClaimsFromToken(token);
            return claimsResolver.apply(claims);
        }catch (Exception e) {
            return null;
        }
    }

    private Date getExpiration(String token) {
        try {
            return getClaim(token, Claims::getExpiration);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isTokenExpired(String token){
        try {
            return getExpiration(token).before(new Date());
        }catch (Exception e){
            return true;
        }

    }
}
