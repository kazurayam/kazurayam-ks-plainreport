import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Collectors

import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.webui.driver.DriverFactory

import groovy.json.JsonSlurper
import com.kazurayam.ks.plainreport.PlainReport

class TLPlainReportCompiler {
	
	private static PlainReport report
	
	static {
		report = new PlainReport()
	}
	
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		report = new PlainReport(testSuiteContext)
		report.setExecutionProfile(RunConfiguration.getExecutionProfile())
		report.setExecutedBrowser(DriverFactory.getExecutedBrowser().toString())
	}
	
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
	}
	
	@AfterTestCase
	def afterTestCase(TestCaseContext testCaseContext) {
		report.addTestCaseContext(testCaseContext)
	}
	
	@AfterTestSuite
	def afterTestSuite(TestSuiteContext testSuiteContext) {
		
		// for debug
		// print the list of files in the Reports folder
		println("@AfterTestSuite")
		listFilesInBasicReportFolder()
		
		// fetch the execution.properties file in the Report folder into the PlainReport object
		Path expro = Paths.get(RunConfiguration.getReportFolder()).resolve('execution.properties')
		Map executionProperties = new JsonSlurper().parse(expro.toFile())
		report.setExecutionProperties(executionProperties)
		
		Path outDir = Paths.get(RunConfiguration.getProjectDir()).resolve('PlainReport')
		serialize(testSuiteContext, outDir)
	}
	
	/**
	 * 
	 * @param testSuiteContext
	 */
	private void serialize(TestSuiteContext testSuiteContext, Path outDir) {
		
		if (!Files.exists(outDir)) {
			Files.createDirectory(outDir)
		}
		
		// serialize the PlainReport object into a JSON file
		Path outPath = outDir.resolve(
			'plainreport_' + 
			testSuiteContext.getTestSuiteId().replace('Test Suites/', '').replace('/', '_') +
			'.json')
		
		outPath.toFile().text = report.toSortedJson()
		
		// print TestCaseContext messages into a plain text file
		Path messagesFile = outDir.resolve(
			"messages_" +
			testSuiteContext.getTestSuiteId().replace('Test Suites/', '').replace('/', '_') +
			'.txt')
		messagesFile.toFile().text = report.printableMessages()
		
		// copy the execution0.log file in the Report folder into the CustomReport folder
		Path source = Paths.get(RunConfiguration.getReportFolder()).resolve('execution0.log')
		Path dest = outDir.resolve('execution0.log')
		Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
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