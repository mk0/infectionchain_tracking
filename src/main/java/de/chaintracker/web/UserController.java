/**
 *
 */
package de.chaintracker.web;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
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
import de.chaintracker.security.aspect.Secured;

/**
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

    final User user = this.userRepository.save(User.builder()
        .email(userCreate.getEmail())
        .encryptedPassword(this.passwordEncoder.encode(userCreate.getPassword()))
        .firstName(userCreate.getFirstName())
        .lastName(userCreate.getLastName())
        .userName(userCreate.getUserName())
        .build());


    return user.getId();
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
  public void deleteAddress(@PathVariable("type") final String type, final Authentication authentication) {

    this.addressRepository.deleteByType(type);
  }
}
