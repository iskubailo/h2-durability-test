package com.iskubailo.h2durabilitytest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.iskubailo.h2durabilitytest.child.ChildApplication;
import com.iskubailo.h2durabilitytest.parent.GlobalStorage;
import com.iskubailo.h2durabilitytest.parent.ParentApplication;

public class H2DurabilityTestApplication {

  private static final String PARENT_PREFIX = "parent:";
  private static final String CHILD_PREFIX = "child:";
  
  public static void main(String[] args) {
    GlobalStorage.originalArguments = args;
    if (hasChildParameter(args)) {
      String[] childArgs = filterArgs(args, false);
      SpringApplication.run(ChildApplication.class, childArgs);
    } else {
      String[] parentArgs = filterArgs(args, true);
      new SpringApplicationBuilder(ParentApplication.class).web(WebApplicationType.NONE).run(parentArgs);
    }
  }

  private static boolean hasChildParameter(String[] args) {
    return Arrays.stream(args).anyMatch("child"::equalsIgnoreCase);
  }
  
  private static String[] filterArgs(String[] source, boolean parent) {
    List<String> result = new ArrayList<>(source.length);
    for (String arg : source) {
      if (arg.startsWith(PARENT_PREFIX)) {
        if (parent) {
          result.add(arg.substring(PARENT_PREFIX.length()));
        }
      } else if (arg.startsWith(CHILD_PREFIX)) {
        if (!parent) {
          result.add(arg.substring(CHILD_PREFIX.length()));
        }
      } else {
        result.add(arg);
      }
    }
    return result.toArray(new String[0]);
  }

}

