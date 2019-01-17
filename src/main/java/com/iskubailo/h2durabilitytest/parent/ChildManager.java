package com.iskubailo.h2durabilitytest.parent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.iskubailo.h2durabilitytest.H2DurabilityTestApplication;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChildManager {
  
  private final RestClient restClient;
  
  private Process process;
  
  @Autowired
  public ChildManager(RestClient restClient) {
    this.restClient = restClient;
  }
  
  public ChildState getState() {
    if (process == null || !process.isAlive()) {
      return ChildState.DOWN;
    }
    log.debug("Rest Request...");
    try {
      String response = restClient.helth();
      log.debug("Rest Response: " + response);
      return ChildState.UP;
    } catch (RestClientException e) {
      log.debug("Rest Error: " + e);
      return ChildState.RUNNING;
    }
  }
  
  public void start() throws IOException {
    process = Runtime.getRuntime().exec(getForkCommand());
    read("OUTPUT", process.getInputStream());
    read("ERROR", process.getErrorStream());
  }
  
  public void kill() throws IOException {
    if (process != null && process.isAlive()) {
      log.warn("Kill H2 app");
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
      log.debug("MAIN: Read " + name + " - started");
      while (scanner.hasNextLine()) {
        log.debug(name + ": " + scanner.nextLine());
      }
      log.debug("MAIN: Read " + name + " - finished");
    };
    new Thread(task).start();
  }
}
