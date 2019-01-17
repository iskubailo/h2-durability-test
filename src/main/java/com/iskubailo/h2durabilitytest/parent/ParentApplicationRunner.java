package com.iskubailo.h2durabilitytest.parent;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class ParentApplicationRunner implements CommandLineRunner {
  
  private final ChildManager childManager;

  public ParentApplicationRunner(ChildManager childManager) {
    this.childManager = childManager;
  }
  
  @Override
  public void run(String... args) throws Exception {
    runTest();
    log.info("Done!");
  }

  private void runTest() throws Exception {
    repeat(5, () -> log.info("State: " + childManager.getState()));
    log.info("Starting...");
    childManager.start();
    repeat(45, () -> log.info("State: " + childManager.getState()));
    log.info("Killing...");
    childManager.destroy();
    repeat(10, () -> log.info("State: " + childManager.getState()));
  }
  
  private void repeat(int times, Runnable runnable) throws InterruptedException {
    for (int i = 0; i < times; i++) {
      log.info("Repeat {}/{}", i, times);
      runnable.run();
      Thread.sleep(1000);
    }
  }

}
