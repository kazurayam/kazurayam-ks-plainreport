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
import my.Memo

class CustomReportCompiler {
	
	static Path outDir
	static Memo memo
	
	static {
		if (outDir == null) {
			outDir = Paths.get(RunConfiguration.getProjectDir()).resolve('CustomReport')
		}
		if (!Files.exists(outDir)) {
			Files.createDirectory(outDir)
		}
		memo = new Memo()
	}
	
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		memo = new Memo(testSuiteContext)
		memo.setExecutionProfile(RunConfiguration.getExecutionProfile())
		memo.setExecutedBrowser(DriverFactory.getExecutedBrowser().toString())
	}
	
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
	}
	
	@AfterTestCase
	def afterTestCase(TestCaseContext testCaseContext) {
		memo.addTestCaseContext(testCaseContext)
	}
	
	@AfterTestSuite
	def afterTestSuite(TestSuiteContext testSuiteContext) {
		// for debug
		// print the list of files in the Reports folder
		println("@AfterTestSuite")
		listFilesInReportFolder()
		
		// fetch the execution.properties file in the Report folder into the Memo object
		Path expro = Paths.get(RunConfiguration.getReportFolder()).resolve('execution.properties')
		Map executionProperties = new JsonSlurper().parse(expro.toFile())
		memo.setExecutionProperties(executionProperties)
		
		// serialize the Memo object into a JSON file
		Path memoFile = outDir.resolve(
			'memo_' + 
			testSuiteContext.getTestSuiteId().replace('Test Suites/', '').replace('/', '_') +
			'.json')
		//memoFile.toFile().text = memo.toJson()
		memoFile.toFile().text = memo.toSortedJson()
		
		// copy the execution0.log file in the Report folder into the CustomReport folder
		Path source = Paths.get(RunConfiguration.getReportFolder()).resolve('execution0.log')
		Path dest = outDir.resolve('execution0.log')
		Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
	}
	
	void listFilesInReportFolder() {
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