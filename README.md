Custom Test Report compiled by TestListener
========

This is a small [Katalon Studio](https://www.katalon.com/download/) project for demonstration purpose.
You can download a zip file of the project from [Releases](https://github.com/kazurayam/CompilingCustomReportInKatalonStudio/releases) page, unzip it, open it with your local Katalon Studio.

The latest version of this project is 0.2.

This project was developed using Katalon Studio version 7.8.1 but it will run with any version above 7.0.

# Problem to solve

In the Katalon User Forum, there was a [question](https://forum.katalon.com/t/get-results-html-path-filename-through-code/50526) that asked how to automatically copy the Test Reports in HTML/PDF format to another location once a test suite finished.

In response to the question, I replied with a [post](https://forum.katalon.com/t/get-results-html-path-filename-through-code/50526/5)
where I described my previous solution. To be honest, I am not very much happy with my previous solution.

Russ Thomas replied a [post](https://forum.katalon.com/t/get-results-html-path-filename-through-code/50526/3) where
he mentioned that, instead of bothering around the built-in Reports, he developed his own reporting functionality. In that post, Russ did not described how his code looks like. So, I was concerned that the readers may feel lost where to go for developing custom reports as Russ did.

# Solution

Katalon Studio provides [CustomReportCompiler](https://docs.katalon.com/katalon-studio/docs/fixtures-listeners.html#test-listeners-test-hooks) which is annotated with the `TestListener` interface.

If you make full use of the TestListener feature, you can compile your own reports of test execution with full control over contents/location/timing. You can compile report in any format you like. You can save the file wherever you want.

I have made a skeletal implementation of my Test Report which is in JSON text format. I used the [Gson](https://github.com/google/gson) library which is bundled in Katalon Studio.

# Description

## How to run the demo

open the Test Suite `Test Suites/TS1`, and just run it.

## Demo output

### Console log

If you look at the console log, you can find output like this:
```
@AfterTestSuite
Reports/20210101_133234/TS1/20210101_133234/execution0.log 30098bytes
Reports/20210101_133234/TS1/20210101_133234/execution0.log.lck 0bytes
Reports/20210101_133234/TS1/20210101_133234/execution.properties 2660bytes
Reports/20210101_133234/TS1/20210101_133234/testCaseBinding 128bytes
```

This message proves that **2 files in the Report folder (`execution.properites` and `execution0.log`) are available at the event of `@AfterTestSuite`.** In these 2 files you can find almost all information out of Katalon Studio how the test suite was configured and how it ran.

### CustomReport dir

Once the `Test Suites/TS1` finished, a new folder `<projectDir>/CustomReport` will be created.
Inside it you will find a 3 files.

- execution0.log
- memo_TS1.json
- messages_TS1.txt

The [`execution0.log`] file is copied from the Reports folder just to for easier reference. In the log file you can find all messages emitted by your tests with a lot of additives including timestamp.

The [`CustomReport/memo_TS1.json`](CustomReport/memo_TS1.json) file contains information from:
- the TestSuiteContext object
- the TestCaseContext objects
- the execution.properties file

The [`CustomReport/messages_TS1.txt`](CustomReport/messages_TS1.txt) file contains messages emitted by failed Test Cases in Java printStackTrace format.

![CustomReportCreated](docs/images/CustomReportCreated0.png)


## How the demo designed

Please read the source of the project to find how the demo designed.

The core part is [`Test Listeners/CustomReportCompiler.groovy`](Test%20Listeners/CustomReportCompiler.groovy). The `CustomReportCompiler` and a custom Groovy class [`my.Memo`](Keywords/my/Memo.groovy) do everything needed to produce the JSON report.

## Desired Reporting frameworks ...

In the Katalon Forum, many people have expressed their wishes that they want to view the test reports using their favorites reporting frameworks. For example;

- https://forum.katalon.com/t/does-katalon-support-integration-of-other-reporting-frameworks-such-as-allure-or-extent-report-if-yes-how/6496

Yes, you can develop your code so that it satisfies your requirements by extracting necessary information and feed it to your favorites reporting framework. That is no different from what I have done here.

However, I would remind you that it would invoke a lot of effort compiling nicely formatted custom report.
