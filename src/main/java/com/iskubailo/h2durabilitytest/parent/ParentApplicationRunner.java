package com.iskubailo.h2durabilitytest.parent;

import java.util.Arrays;
import java.util.OptionalLong;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.web.client.RestClientException;

import com.iskubailo.h2durabilitytest.child.DataDto;
import com.iskubailo.h2durabilitytest.child.DataEntity;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class ParentApplicationRunner implements CommandLineRunner {
  
  private final ChildManager childManager;
  private final RestClient restClient;
  private final TestContext context = new TestContext();

  public ParentApplicationRunner(ChildManager childManager, RestClient restClient) {
    this.childManager = childManager;
    this.restClient = restClient;
  }
  
  @Override
  public void run(String... args) throws Exception {
    log.info("--------------------------------------------------");
    log.debug("Original Arguments: " + Arrays.toString(GlobalStorage.originalArguments));
    log.debug("Parent Arguments: " + Arrays.toString(args));
    while (true) {
      runTest();
    }
  }

  private void runTest() throws Exception {
    log.info("Starting H2 app...");
    childManager.start();
    repeatUntil(() -> childManager.getState() == ChildState.UP);
    
    try {

      DataDto selected = restClient.select();
      log.info("Selected: " + selected);
      if (context.getLastId() > 0) {
        if (lastIdValid(selected)) {
          log.info("Test: SUCCESS");
          context.succeed();
        } else {
          log.info("Test: FAILURE");
          context.failed();
        }
      } else {
        log.info("Last Test: No result (first run)");
      }
      
      DataEntity inserted = restClient.insert();
      log.info("Inserted: " + inserted);
      context.setLastId(inserted.getId());
      
      String halted = restClient.exit();
      log.info("Exit: " + halted);
      
      repeatUntil(() -> childManager.getState() == ChildState.DOWN);
      log.info("H2 app down");
      
      log.info("Result: " + context);
      log.info("");
      
    } catch(RestClientException e) {
      log.error("Communication Error", e);
    } finally {
      context.nextTest();
      childManager.kill();
    }
  }

  private boolean lastIdValid(DataDto dataDto) {
    OptionalLong lastId = dataDto.getList().stream().mapToLong(DataEntity::getId).findFirst();
    return (lastId.isPresent() && lastId.getAsLong() == context.getLastId());
  }
  
  private void repeatUntil(Callable<Boolean> callable) throws Exception {
    boolean finished = false;
    while (!finished) {
      finished = callable.call();
      if (!finished) {
        TimeUnit.MILLISECONDS.sleep(1000);
      }
    }
  }

}
