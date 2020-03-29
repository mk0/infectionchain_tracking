/**
 *
 */
package de.chaintracker.util;

import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import de.chaintracker.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Marko Voß
 *
 */
@Component
public class JWTHelper {

  @Value("${app.token.secret}")
  private String tokenSecret;

  public String createJWT(final String userId, final String userEmail) {

    final Claims claims = Jwts.claims().setSubject(userEmail);
    claims.put(SecurityConstants.JWT_CLAIM_USERID, userId);

    return Jwts.builder().setSubject(userEmail)
        .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .signWith(SignatureAlgorithm.HS512, this.tokenSecret).setClaims(claims)
        .compact();
  }
}
