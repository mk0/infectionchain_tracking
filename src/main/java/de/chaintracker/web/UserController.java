/**
 *
 */
package de.chaintracker.web;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.chaintracker.dto.UserAddressDto;
import de.chaintracker.dto.UserCreateDto;
import de.chaintracker.dto.UserDto;
import de.chaintracker.entity.User;
import de.chaintracker.entity.UserAddress;
import de.chaintracker.repo.UserAddressRepository;
import de.chaintracker.repo.UserRepository;
import de.chaintracker.security.SecurityConstants;
import de.chaintracker.security.aspect.Secured;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * TODO: Restrict edit methods to verified {@link User}s only.
 *
 * @author Marko Voß
 *
 */
@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserAddressRepository addressRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Value("${app.token.secret}")
  private String tokenSecret;

  @Secured
  @RequestMapping(method = RequestMethod.GET)
  public UserDto getUser(final Authentication authentication) {

    return UserDto.fromEntity(this.userRepository.findByEmail(authentication.getName()).get());
  }

  @RequestMapping(method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
  public String createUser(@RequestBody final UserCreateDto userCreate) {

    if (this.userRepository.existsByEmail(userCreate.getEmail()))
      throw new IllegalArgumentException("Email does already exist.");

    if (this.userRepository.existsByUserName(userCreate.getUserName()))
      throw new IllegalArgumentException("Username does already exist.");

    final String verificationJwt = Jwts.builder().setSubject(userCreate.getEmail())
        .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .signWith(SignatureAlgorithm.HS512, this.tokenSecret).compact();

    final User user = this.userRepository.save(User.builder()
        .email(userCreate.getEmail())
        .emailVerificationToken(verificationJwt)
        .encryptedPassword(this.passwordEncoder.encode(userCreate.getPassword()))
        .firstName(userCreate.getFirstName())
        .lastName(userCreate.getLastName())
        .userName(userCreate.getUserName())
        .build());

    // TODO: Email verification: Send email

    return user.getId();
  }

  @RequestMapping(method = RequestMethod.POST, path = "/email-verification")
  public void verifyEmail(@RequestBody final String verificationJwt) {

    final Claims claims = Jwts.parser().setSigningKey(this.tokenSecret).parseClaimsJws(verificationJwt).getBody();
    final String email = claims.getSubject();
    final Date expiration = claims.getExpiration();

    if (expiration.after(new Date())) {
      /*
       * Delete account to enable new registration using the same unique fields if and only if account has
       * not yet been enabled.
       */
      this.userRepository.deleteByEmailAndEmailVerificationStatus(email, false);
      return; // ignore invalid requests
    }

    final Optional<User> userOp = this.userRepository.findByEmail(email);

    if (userOp.isEmpty())
      return; // ignore invalid requests

    final User user = userOp.get();
    user.setEmailVerificationStatus(true);

  }

  @Secured
  @RequestMapping(method = RequestMethod.GET, path = "/qr-code")
  public String getQrCode(final Authentication authentication) {
    return this.userRepository.findByEmail(authentication.getName()).get().getQrCode();
  }

  @Secured
  @RequestMapping(method = RequestMethod.POST, path = "/address")
  public void postAddress(@RequestBody final UserAddressDto address, final Authentication authentication) {

    final User user = this.userRepository.findByEmail(authentication.getName()).get();

    final Optional<UserAddress> existingAddressOp =
        this.addressRepository.findByUserIdAndType(user.getId(), address.getType());

    if (existingAddressOp.isPresent()) {
      // update

      final UserAddress existingAddress = existingAddressOp.get();
      existingAddress.setCity(address.getCity());
      existingAddress.setCountry(address.getCountry());
      existingAddress.setPostalCode(address.getPostalCode());
      existingAddress.setStreetname(address.getStreetname());
      existingAddress.setUserUpdate(user);
      existingAddress.setUserUpdate(user);

      this.addressRepository.save(existingAddress);

    } else {
      // create

      this.addressRepository.save(UserAddress.builder()
          .addressExternalId(UUID.randomUUID().toString())
          .city(address.getCity())
          .country(address.getCountry())
          .postalCode(address.getPostalCode())
          .streetname(address.getStreetname())
          .type(address.getType())
          .user(user)
          .userCreate(user)
          .build());

    }
  }

  @Secured
  @RequestMapping(method = RequestMethod.GET, path = "/address")
  public List<UserAddressDto> getAddresses(final Authentication authentication) {

    final User user = this.userRepository.findByEmail(authentication.getName()).get();

    return this.addressRepository.findByUserId(user.getId()).stream().map(UserAddressDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Secured
  @RequestMapping(method = RequestMethod.DELETE, path = "/address/{type}")
  @Transactional
  public void deleteAddress(@PathVariable("type") final String type) {

    this.addressRepository.deleteByType(type);
  }
}
