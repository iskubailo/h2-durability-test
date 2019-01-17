package com.iskubailo.h2durabilitytest.child;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class DataEntity {
  @Id
  @GeneratedValue
  private Long id;
  @Column
  private String data;
}
