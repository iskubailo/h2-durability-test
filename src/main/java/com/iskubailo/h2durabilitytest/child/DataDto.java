package com.iskubailo.h2durabilitytest.child;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataDto {
  private List<DataEntity> list;
}
