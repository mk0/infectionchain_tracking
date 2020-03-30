package de.chaintracker.util;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Random;
import org.springframework.stereotype.Component;

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

  public String generateUniqueId(final int length) {
    return generateRandomString(length);
  }

  private String generateRandomString(final int length) {
    final StringBuilder returnValue = new StringBuilder(length);

    for (int i = 0; i < length; i++) {
      returnValue.append(this.ALPHABET.charAt(this.RANDOM.nextInt(this.ALPHABET.length())));
    }

    return new String(returnValue);

  }
}
