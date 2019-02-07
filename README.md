# H2 Durability Test

This application was developed to test durability of embedded H2 database when using with spring-boot application to eliminate the problem I faced with.

### Problem

When developing spring boot application with embedded H2 database I noticed that after killing the app sometimes (not always) persisted data were lost after restart. To investigate this problem, try to understand the reason and find a way how to avoid it this application was developed.

### Architecture

The application is a spring boot application and consist of two parts: Parent and Child sub applications.

Parent Application is responsible for test execution, collecting test result and starting and stopping Child Application. It starts the Child Application, execute tests, collect results, shutdown Child Application then repeat the loop forever.

Child Application starts simple rest api that allows insert and select simple data into/from embedded h2 database, which is used by Parent Application to perform a test. And also provide endpoints to shutdown itself with several methods.

### Starting the application

To start the application just execute:
```
java -jar h2-durability-test-0.0.1-SNAPSHOT.jar
```
it will start with default configuration.
It will store database on the disk in `<current-dir>/data/` folder in file `storage.mv.db`.

### Parameters

Sometimes default configuration is not enough, so you can customize it according to your needs.

Configure different rest http post (default: 8080):
```
--server.port=8081
```

Change default timeout (in milliseconds) Parent Application waits for the response from Child Application (default: 3000 ms)
```
--app.rest.timeout=60000
```

Since this is normal spring boot application it also accepts any other property spring boot provides. For example to configure logging:
```
--logging.file=data/application.log
--logging.level.com.iskubailo=DEBUG
```
This could be useful for troubleshooting. By default when Parent Application starts it logs only Parent Application part. But when Child Application failed to start (for example port is already used by another process) you will not noticed that, since no logs from Child Application are provided. To include logs from Child Application as well as more information from Parent Application use `DEBUG` log level.

If this is not enough you can even increase verbosity by using `TRACE` log level:
```
--logging.level.com.iskubailo=TRACE
```

### Parent vs Child Application Parameters

Sometimes you need to pass different parameters to Parent and Child Applications. You can achieve this by providing `parent:` or `child:` prefix. This could be useful for logs for example:
```
parent:--logging.file=data/parent.log
child:--logging.file=data/child.log
```

### Main Class Detection

After you launch Parent Application it should know main class to be able to launch Child Application. To launch Child Application the same main class as for Parent Application is used, but with `child` parameter. The Parent Application tries to automatically detect the main class in runtime by using JAVA_MAIN_CLASS environment variable, but it is not always available ([link](https://stackoverflow.com/questions/939932/how-to-determine-main-class-at-runtime-in-threaded-java-application)). And if this is the case you need to provide the main class by yourself, by providing the following parameter:
```
main:org.springframework.boot.loader.JarLauncher
```

One of the idea was to hardcode main class, but depending on how you run the application (from IDE or uber spring boot jar) the main class is different.

### Stop methods

The core intention of this app is to test durability of H2 under different stop method. Here is a list of supported stop method. To choose stop method just pass relevant parameter to the application:

`stop-exit` - Parent Application execute “/exit” rest api on Child Application which triggers `System.exit(0)`. This method executes shutdown hooks before stopping virtual machine.

`stop-halt` - Parent Application execute “/halt” rest api on Child Application which triggers `Runtime.getRuntime().halt(0)`. Unlike the exit method, this method does not cause shutdown hooks to be launched.

`stop-kill` - Child Application is started by using `Runtime.getRuntime().exec(command)` command which returns `Process`. This method executes `process.destroy()` to shutdown Child Application.

`stop-kill-f` - As the previous method this one uses `process.destroyForcibly()` to shutdown Child Application.

### Command

Example of command with rich parameters set to launch the test:
```
java -jar h2-durability-test-0.0.1-SNAPSHOT.jar main:org.springframework.boot.loader.JarLauncher --server.port=8801 parent:--logging.file=data/parent.log child:--logging.file=data/child.log --app.rest.timeout=60000 stop-exit
```

### Test results

[See test results](results/README.md)
