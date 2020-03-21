/**
 *
 */
package de.chaintracker.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import de.chaintracker.dto.UserDto;
import de.chaintracker.repo.UserRepository;

/**
 * @author Marko Voß
 *
 */
@RestController("/user")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @GetMapping()
  public UserDto getUser(@PathVariable("userId") final String userId) {

    return UserDto.fromEntity(this.userRepository.findById(userId).get());
  }


}
