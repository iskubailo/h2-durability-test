package com.iskubailo.h2durabilitytest;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.iskubailo.h2durabilitytest.child.ChildApplication;
import com.iskubailo.h2durabilitytest.parent.ParentApplication;

public class H2DurabilityTestApplication {

  public static void main(String[] args) {
    Optional<String> child = Arrays.stream(args).filter("child"::equalsIgnoreCase).findFirst();
    
    if (child.isPresent()) {
      String[] childArgs = filterArgs(args, "parent:");
      SpringApplication.run(ChildApplication.class, childArgs);
    } else {
      String[] parentArgs = filterArgs(args, "child:");
      new SpringApplicationBuilder(ParentApplication.class).web(WebApplicationType.NONE).run(parentArgs);
    }
  }
  
  private static String[] filterArgs(String[] source, String prefix) {
    return Stream.of(source).filter(arg -> !arg.startsWith(prefix)).toArray(i -> new String[i]);
  }

}

