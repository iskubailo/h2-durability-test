package com.iskubailo.h2durabilitytest;

import org.springframework.boot.SpringApplication;

import com.iskubailo.h2durabilitytest.child.ChildApplication;

public class H2DurabilityTestApplication {

  public static void main(String[] args) {
    SpringApplication.run(ChildApplication.class, args);
  }

}

