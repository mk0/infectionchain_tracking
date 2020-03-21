package de.chaintracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChaintrackerBackendApplication {

  private static final Logger log = LoggerFactory.getLogger(ChaintrackerBackendApplication.class);

  public static void main(final String[] args) {
    SpringApplication.run(ChaintrackerBackendApplication.class, args);
  }

}
