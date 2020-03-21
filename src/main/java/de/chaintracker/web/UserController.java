/**
 *
 */
package de.chaintracker.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import de.chaintracker.dto.UserCreateDto;
import de.chaintracker.dto.UserDto;
import de.chaintracker.entity.User;
import de.chaintracker.repo.UserRepository;

/**
 * @author Marko Voß
 *
 */
@RestController("/user")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping
  public UserDto getUser(@PathVariable("userId") final String userId) {

    return UserDto.fromEntity(this.userRepository.findById(userId).get());
  }

  @PostMapping
  public String createUser(final UserCreateDto userCreate) {

    if (this.userRepository.existsByEmail(userCreate.getUser().getEmail()))
      throw new IllegalArgumentException("Email does already exist.");

    if (this.userRepository.existsByUserName(userCreate.getUser().getUserName()))
      throw new IllegalArgumentException("Username does already exist.");

    return this.userRepository.save(User.builder()
        .email(userCreate.getUser().getEmail())
        .encryptedPassword(this.passwordEncoder.encode(userCreate.getPassword()))
        .firstName(userCreate.getUser().getFirstName())
        .lastName(userCreate.getUser().getLastName())
        .userName(userCreate.getUser().getUserName())
        .build()).getId();
  }
}
