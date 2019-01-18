package com.iskubailo.h2durabilitytest.parent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.iskubailo.h2durabilitytest.child.DataDto;
import com.iskubailo.h2durabilitytest.child.DataEntity;

@Component
public class RestClient {
  private final RestTemplate restTemplate;
  private final String serverPort;

  public RestClient(RestTemplate restTemplate, @Value("${server.port}") String serverPort) {
    this.restTemplate = restTemplate;
    this.serverPort = serverPort;
  }
  
  public String helth() {
    return restTemplate.getForObject(getBaseUrl() + "/", String.class);
  }

  public DataDto select() {
    return restTemplate.getForObject(getBaseUrl() + "/select", DataDto.class);
  }
  
  public DataEntity insert() {
    return restTemplate.getForObject(getBaseUrl() + "/insert", DataEntity.class);
  }
  
  public String exit() {
    return restTemplate.getForObject(getBaseUrl() + "/exit", String.class);
  }
  
  public String halt() {
    return restTemplate.getForObject(getBaseUrl() + "/halt", String.class);
  }
  
  private String getBaseUrl() {
    return "http://localhost:" + serverPort;
  }
  
}
