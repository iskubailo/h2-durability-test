package com.iskubailo.h2durabilitytest.parent;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class ParentApplication {
  
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, @Value("${app.rest.timeout}") long restTimeout) {
    return restTemplateBuilder
        .setConnectTimeout(Duration.ofMillis(restTimeout))
        .setReadTimeout(Duration.ofMillis(restTimeout))
        .build();
  }
}
