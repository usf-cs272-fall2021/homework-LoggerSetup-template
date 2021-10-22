LoggerSetup
=================================================

![Points](../../blob/badges/points.svg)

For this homework, you will setup and configure the `log4j2` logging library. The **only thing** you should do for this homework assignment is import the homework and create a `log4j2` configuration file in the correct location with the correct configuration.

## Configuration

The `log4j2` third-party package should be setup automatically by `maven` when you import this homework into Eclipse.

Configure the root logger to output `WARN` messages and higher to the console only (no output to the file). Configure the class-specific `LoggerSetup` logger to output `INFO` messages and higher (more severe) to the console and `ALL` messages to a `debug.log` file in the current working directory. You can use the `log4j2.xml` example from lecture as a starting point.

Only output the message and **short** error message (if appropriate) to the console. The expected console output should look like:

```
ROOT LOGGER:
ERROR emu
Catching falcon

CLASS LOGGER:
ibis
wren
ERROR emu
Catching falcon
```

Include the sequence number, level (using only 3 letters), file, line number, thread name, 3 lines from any throwable stack trace (if appropriate), and a newline character (`%n`) in the `debug.log` file. See the `test/resources/debug.log` file for the expected file output. It is also included below:

```
[01 trace] LoggerSetup.java:20 main: turkey 
[02 debug] LoggerSetup.java:21 main: duck 
[03 info] LoggerSetup.java:22 main: ibis 
[04 warn] LoggerSetup.java:23 main: wren 
[05 error] LoggerSetup.java:24 main: ERROR java.lang.Exception: emu
	at LoggerSetup.outputMessages(LoggerSetup.java:24)
	at LoggerSetup.main(LoggerSetup.java:39)
[06 fatal] LoggerSetup.java:25 main: Catching java.lang.RuntimeException: falcon
	at LoggerSetup.outputMessages(LoggerSetup.java:25)
	at LoggerSetup.main(LoggerSetup.java:39)
```

You should **NOT** modify the `LoggerSetup.java`, `LoggerSetup.java`, or `test/debug.log` files. You should only create a **NEW** file in the correct location to configure log4j2.

## Hints ##

Below are some hints that may help with this homework assignment:

  - The `log4j2.xml` file in the [lecture examples](https://github.com/usf-cs272-fall2021/lectures/blob/main/Debugging/src/main/resources/log4j2.xml) is a good starting point.

  - For configuring the output locations (where to output, file versus console), take a look at the [ConsoleAppender](https://logging.apache.org/log4j/2.x/manual/appenders.html#ConsoleAppender) and [FileAppender](https://logging.apache.org/log4j/2.x/manual/appenders.html#FileAppender) information pages.

  - For configuring the output format (what to output), I recommend you take a look at the [PatternLayout](https://logging.apache.org/log4j/2.x/manual/layouts.html#PatternLayout) information page. It includes all of the possible patterns, like `class`, `date`, `throwable` :star:, `file`, `location`, `line`, `message`, `method`, `n`, `level`, `sequenceNumber`, `threadId`, and `threadName` (you will only use some of these).

  - **Do NOT overwrite the `test/debug.log` file. You should configure log4j2 to write to the path `debug.log` instead.**

These hints are *optional*. There may be multiple approaches to solving this homework.

## Requirements ##

See the Javadoc and `TODO` comments in the template code in the `src/main/java` directory for additional details. You must pass the tests provided in the `src/test/java` directory. Do not modify any of the files in the `src/test` directory.

See the [Homework Guides](https://usf-cs272-fall2021.github.io/guides/homework/) for additional details on homework requirements and submission.
