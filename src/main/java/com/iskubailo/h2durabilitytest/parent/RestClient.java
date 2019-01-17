package com.iskubailo.h2durabilitytest.parent;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.iskubailo.h2durabilitytest.child.DataDto;

@Component
public class RestClient {
  private final RestTemplate restTemplate;

  public RestClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
  
  public DataDto select() {
    return restTemplate.getForObject("http://localhost:8080/select", DataDto.class);
  }
}
