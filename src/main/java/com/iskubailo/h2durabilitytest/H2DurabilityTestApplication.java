package com.iskubailo.h2durabilitytest;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.iskubailo.h2durabilitytest.child.ChildApplication;
import com.iskubailo.h2durabilitytest.parent.ParentApplication;

public class H2DurabilityTestApplication {

  public static void main(String[] args) {
    Optional<String> child = Arrays.stream(args).filter("child"::equalsIgnoreCase).findFirst();
    
    if (child.isPresent()) {
      SpringApplication.run(ChildApplication.class, args);
    } else {
      new SpringApplicationBuilder(ParentApplication.class).web(WebApplicationType.NONE).run(args);
    }
  }

}

