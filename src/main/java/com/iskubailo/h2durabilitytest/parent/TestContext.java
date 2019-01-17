package com.iskubailo.h2durabilitytest.parent;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class TestContext {
  private long number = 0;
  private long success = 0;
  private long failure = 0;
  
  @Getter @Setter
  private long lastId = 0;
  
  public void nextTest() {
    number++;
  }

  public void succeed() {
    success++;
  }

  public void failed() {
    failure++;
  }
}
