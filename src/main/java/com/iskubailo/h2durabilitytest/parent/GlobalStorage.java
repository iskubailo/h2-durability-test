package com.iskubailo.h2durabilitytest.parent;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GlobalStorage {
  public static volatile String[] originalArguments;
}
