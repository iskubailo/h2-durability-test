package com.iskubailo.h2durabilitytest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.iskubailo.h2durabilitytest.child.ChildApplication;
import com.iskubailo.h2durabilitytest.parent.ParentApplication;

public class H2DurabilityTestApplication {

  public static void main(String[] args) {
    if (args.length == 0) {
      new SpringApplicationBuilder(ParentApplication.class)
          .web(WebApplicationType.NONE)
          .run(args);
    } else if (args.length == 1 && "child".equalsIgnoreCase(args[0])) {
      SpringApplication.run(ChildApplication.class, args);
    } else {
      throw new IllegalArgumentException("Wrong arguments");
    }
  }

}

