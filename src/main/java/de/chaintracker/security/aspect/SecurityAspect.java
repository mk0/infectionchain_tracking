/**
 *
 */
package de.chaintracker.security.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author Marko Voß
 *
 */
@Aspect
@Component
public class SecurityAspect {

  @Around("@annotation(Secured)")
  public Object logExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {

    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof UsernamePasswordAuthenticationToken) || !authentication.isAuthenticated())
      throw new InsufficientAuthenticationException("Authentication required.");

    return joinPoint.proceed();
  }
}
