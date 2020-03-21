package de.chaintracker.util;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import de.chaintracker.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Utils {

	public final Random RANDOM = new SecureRandom();
	public final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	public final int CURRENTYEAR = Calendar.getInstance().get(Calendar.YEAR);
	public final int CURRENTMONTH = Calendar.getInstance().get(Calendar.MONTH);

	// Prefix related to Contact Event
	public final String ContactEventExternalIdPrefix = "CEV";

	// Prefix related to Country
	public final String CountryExternalIdPrefix = "CTY";

	// Prefix related to Location Event
	public final String LocationEventExternalIdPrefix = "LCE";

	// Prefix related to User Address
	public final String UserAddressExternalIdPrefix = "UAD";

	public String generateUniqueId(int length) {
		return generateRandomString(length);
	}

	private String generateRandomString(int length) {
		StringBuilder returnValue = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}

		return new String(returnValue);

	}

	public static boolean hasTokenExpired(String token) {

		Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token).getBody();

		Date tokenExpirationDate = claims.getExpiration();
		Date todayDate = new Date();

		return tokenExpirationDate.before(todayDate);
	}

	public String generateEmailVerificationToken(String userId) {

		String token = Jwts.builder().setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret()).compact();

		return token;

	}

	public String getUserIdFromToken(String token) {

		String authToken = token.toString();
		// System.out.print(authToken);

		String cleanToken = authToken.replace("Bearer ", "");

		Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(cleanToken)
				.getBody();

		// System.out.print("Test "+ claims.get("userId"));

		String userId = claims.getSubject();

		return new String(userId);

	}

}
