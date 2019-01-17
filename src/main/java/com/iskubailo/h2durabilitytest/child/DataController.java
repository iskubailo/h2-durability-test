package com.iskubailo.h2durabilitytest.child;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {
  private DataRepository dataRepository;
  
  public DataController(DataRepository dataRepository) {
    this.dataRepository = dataRepository;
  }
  
  @RequestMapping("/")
  public String root() {
    return "UP";
  }
  
  @RequestMapping("/insert")
  public DataEntity insert() {
    DataEntity entity = new DataEntity();
    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").format(new Date());
    entity.setData(date);
    entity = dataRepository.save(entity);
    return entity;
  }

  @RequestMapping("/select")
  public DataDto select() {
    List<DataEntity> list = dataRepository.findAll(PageRequest.of(0, 3, Direction.DESC, "id")).getContent();
    return new DataDto(list);
  }

  @GetMapping("/exit")
  public String exit() {
    execDelayed(() -> System.exit(0));
    return "OK - System.exit(0) in 5 sec";
  }
  
  @GetMapping("/halt")
  public String halt() {
    execDelayed(() -> Runtime.getRuntime().halt(0));
    return "OK - Runtime.getRuntime().halt(0) in 5 sec";
  }
  
  private void execDelayed(Runnable runnable) {
    new Thread(() -> {
      try {
        TimeUnit.SECONDS.sleep(5);
      } catch (InterruptedException e) {
        // ignore
      }
      runnable.run();
    }).start();
  }
}
