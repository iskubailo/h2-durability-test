package com.iskubailo.h2durabilitytest.parent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

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
      log.debug("STATE: DOWN");
      return ChildState.DOWN;
    }
    log.trace("Rest Request...");
    try {
      String response = restClient.helth();
      log.trace("Rest Response: {}", response);
      log.debug("STATE: UP");
      return ChildState.UP;
    } catch (RestClientException e) {
      log.trace("Rest Error: {}", e.toString());
      log.debug("STATE: RUNNING");
      return ChildState.RUNNING;
    }
  }
  
  public void start() throws IOException {
    String command = getForkCommand();
    log.debug("Command: {}", command);
    process = Runtime.getRuntime().exec(command);
    read("OUTPUT", process.getInputStream());
    read("ERROR", process.getErrorStream());
  }
  
  public void kill() throws IOException {
    if (process != null && process.isAlive()) {
      log.warn("Kill H2 app");
      process.destroy();
    }
  }
  
  private String getForkCommand() {
    StringBuilder cmd = new StringBuilder();
    cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
    for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
      if (jvmArg.contains("agentlib:jdwp") || jvmArg.contains("-Xrunjdwp:") || jvmArg.contains("-javaagent:")) {
        jvmArg = "";
      }
      cmd.append(jvmArg + " ");
    }
    cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
    String mainClass = getMainClass();
    log.debug("MainClass: {}", mainClass);
    cmd.append(mainClass).append(" child ");
    for (String arg : GlobalStorage.originalArguments) {
      cmd.append(arg).append(" ");
    }
    return cmd.toString();
  }
  
  private String getMainClass() {
    for (final Map.Entry<String, String> entry : System.getenv().entrySet())
      if (entry.getKey().startsWith("JAVA_MAIN_CLASS")) // like JAVA_MAIN_CLASS_13328
        return entry.getValue();
    throw new IllegalStateException("Cannot determine main class.");
  }

  private void read(String name, InputStream inputStream) {
    Runnable task = () -> {
      try (Scanner scanner = new Scanner(inputStream)) {
        log.debug("MAIN: Read " + name + " - started");
        while (scanner.hasNextLine()) {
          log.debug(name + ": " + scanner.nextLine());
        }
        log.debug("MAIN: Read " + name + " - finished");
      }
    };
    new Thread(task).start();
  }
}
