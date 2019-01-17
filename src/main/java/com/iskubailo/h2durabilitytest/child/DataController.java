package com.iskubailo.h2durabilitytest.child;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
  public List<DataEntity> select() {
    return dataRepository.findAll(PageRequest.of(0, 10, Direction.DESC, "id")).getContent();
  }

  @GetMapping("/exit")
  public String exit() {
    System.exit(0);
    return "System.exit(0)";
  }
  
  @GetMapping("/halt")
  public String halt() {
    Runtime.getRuntime().halt(0);
    return "Runtime.getRuntime().halt(0)";
  }
}
