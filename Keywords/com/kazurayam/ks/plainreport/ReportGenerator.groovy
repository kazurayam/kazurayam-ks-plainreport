package com.kazurayam.ks.plainreport

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Collectors

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.webui.driver.DriverFactory
import org.apache.commons.io.FileUtils

import groovy.json.JsonSlurper


public class ReportGenerator {

	private PlainReport report

	private Path outputDir = Paths.get(RunConfiguration.getProjectDir()).resolve('PlainReport')

	public ReportGenerator() {
		report = new PlainReport()
	}

	public void setOutputDir(Path outputDir) {
		Objects.requireNonNull(outputDir)
		this.outputDir = outputDir
	}

	public void setOutputDir(String outputDir) {
		this.setOutputDir(Paths.get(outputDir))
	}

	public void setOutputDir(File outputDir) {
		this.setOutputDir(outputDir.toPath())
	}

	public void beforeTestSuite(TestSuiteContext testSuiteContext) {
		report = new PlainReport(testSuiteContext)
		report.setExecutionProfile(RunConfiguration.getExecutionProfile())
		report.setExecutedBrowser(DriverFactory.getExecutedBrowser().toString())
	}

	public void beforeTestCase(TestCaseContext testCaseContext) {
		// nothing to do
	}

	public void afterTestCase(TestCaseContext testCaseContext) {
		report.addTestCaseContext(testCaseContext)
	}

	public void afterTestSuite(TestSuiteContext testSuiteContext) {
		// for debug
		// print the list of files in the Reports folder
		println("@AfterTestSuite")
		listFilesInBasicReportFolder()

		// fetch the execution.properties file in the Report folder into the PlainReport object
		Path expro = Paths.get(RunConfiguration.getReportFolder()).resolve('execution.properties')
		Map executionProperties = new JsonSlurper().parse(expro.toFile())
		report.setExecutionProperties(executionProperties)

		serialize(testSuiteContext, outputDir)
	}

	/**
	 *
	 * @param testSuiteContext
	 */
	private void serialize(TestSuiteContext testSuiteContext, Path dir) {

		if (Files.exists(dir)) {
			// delete files which were previously created
			FileUtils.deleteDirectory(dir.toFile())
		}
		
		Files.createDirectory(dir)

		// copy the execution0.log file in the Report folder into the CustomReport folder
		Path reportFolder = Paths.get(RunConfiguration.getReportFolder())
		Path source = reportFolder.resolve('execution0.log')
		Path dest = dir.resolve('execution0.log')
		Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
		
		String timestamp = reportFolder.getFileName().toString()   // e.g, 20210318_185922
		
		// serialize the PlainReport object into a JSON file
		Path outPath = dir.resolve(
				'plainreport.' +
				testSuiteContext.getTestSuiteId().replace('Test Suites/', '').replace('/', '_') +
				'.' + timestamp +
				'.json')

		outPath.toFile().text = report.toSortedJson()

		// print TestCaseContext messages into a plain text file
		Path messagesFile = dir.resolve(
				"messages." +
				testSuiteContext.getTestSuiteId().replace('Test Suites/', '').replace('/', '_') +
				'.' + timestamp +
				'.txt')
		messagesFile.toFile().text = report.printableMessages()

		
	}

	/**
	 *
	 */
	private void listFilesInBasicReportFolder() {
		Path projectDir = Paths.get(RunConfiguration.getProjectDir())
		Path reportFolder = Paths.get(RunConfiguration.getReportFolder())
		Set files = Files.list(reportFolder)
				.filter { file -> !Files.isDirectory(file) }
				.collect(Collectors.toSet())
		for (Path f in files) {
			Path relativePath = projectDir.relativize(f)
			println("${relativePath} ${f.toFile().length()} bytes")
		}
	}
}