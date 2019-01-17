package com.iskubailo.h2durabilitytest.parent;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.iskubailo.h2durabilitytest.child.DataDto;
import com.iskubailo.h2durabilitytest.child.DataEntity;

@Component
public class RestClient {
  private final RestTemplate restTemplate;

  public RestClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
  
  public String helth() {
    return restTemplate.getForObject("http://localhost:8080/", String.class);
  }
  
  public DataDto select() {
    return restTemplate.getForObject("http://localhost:8080/select", DataDto.class);
  }
  
  public DataEntity insert() {
    return restTemplate.getForObject("http://localhost:8080/insert", DataEntity.class);
  }
  
  public String exit() {
    return restTemplate.getForObject("http://localhost:8080/exit", String.class);
  }
  
  public String halt() {
    return restTemplate.getForObject("http://localhost:8080/halt", String.class);
  }
  
}
