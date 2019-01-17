package com.iskubailo.h2durabilitytest.parent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.iskubailo.h2durabilitytest.H2DurabilityTestApplication;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChildManager {
  
  private final RestTemplate restTemplate;
  
  private Process process;
  
  @Autowired
  public ChildManager(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
  
  public ChildState getState() {
    if (process == null || !process.isAlive()) {
      return ChildState.DOWN;
    }
    log.info("Rest Request...");
    try {
      String response = restTemplate.getForObject("http://localhost:8080/select", String.class);
      log.info("Rest Response: " + response);
      return ChildState.UP;
    } catch (RestClientException e) {
      log.info("Rest Error: " + e);
      return ChildState.RUNNING;
    }
  }
  
  public void start() throws IOException {
    process = Runtime.getRuntime().exec(getForkCommand());
    read("OUTPUT", process.getInputStream());
    read("ERROR", process.getErrorStream());
  }
  
  public void destroy() throws IOException {
    if (process != null) {
      process.destroy();
    }
  }
  
  private static String getForkCommand() {
    StringBuilder cmd = new StringBuilder();
    cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
    for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
        cmd.append(jvmArg + " ");
    }
    cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
    cmd.append(H2DurabilityTestApplication.class.getName()).append(" child");
    return cmd.toString();
  }

  private static void read(String name, InputStream inputStream) {
    Scanner scanner = new Scanner(inputStream);
    Runnable task = () -> {
      log.info("MAIN: Read " + name + " - started");
      while (scanner.hasNextLine()) {
        log.info(name + ": " + scanner.nextLine());
      }
      log.info("MAIN: Read " + name + " - finished");
    };
    new Thread(task).start();
  }
}
