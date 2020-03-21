/**
 *
 */
package de.chaintracker.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.chaintracker.dto.UserCreateDto;
import de.chaintracker.dto.UserDto;
import de.chaintracker.entity.User;
import de.chaintracker.repo.UserRepository;

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
  private PasswordEncoder passwordEncoder;

  @RequestMapping(method = RequestMethod.GET)
  public UserDto getUser() {

    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken))
      return UserDto.fromEntity(this.userRepository.findByEmail(authentication.getName()).get());

    throw new IllegalStateException();
  }

  @RequestMapping(method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
  public String createUser(@RequestBody final UserCreateDto userCreate) {

    if (this.userRepository.existsByEmail(userCreate.getEmail()))
      throw new IllegalArgumentException("Email does already exist.");

    if (this.userRepository.existsByUserName(userCreate.getUserName()))
      throw new IllegalArgumentException("Username does already exist.");

    return this.userRepository.save(User.builder()
        .email(userCreate.getEmail())
        .encryptedPassword(this.passwordEncoder.encode(userCreate.getPassword()))
        .firstName(userCreate.getFirstName())
        .lastName(userCreate.getLastName())
        .userName(userCreate.getUserName())
        .build()).getId();
  }
}
