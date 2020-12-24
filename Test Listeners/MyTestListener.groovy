import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext

import my.Memo

class MyTestListener {
	
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
		Gson gson = new GsonBuilder().setPrettyPrinting().create()
		String json = gson.toJson(testSuiteContext)
		Path beforeTSReport = outDir.resolve('before_' + testSuiteContext.getTestSuiteId().replace('/', '_') + '.json')
		beforeTSReport.toFile().text = json
		//
		memo = new Memo(testSuiteContext)
		memo.setExecutionProfile(RunConfiguration.getExecutionProfile())
	}
	
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create()
		String json = gson.toJson(testCaseContext)
		Path beforeTCReport = outDir.resolve('before_' + testCaseContext.getTestCaseId().replace('/', '_') + '.json')
		beforeTCReport.toFile().text = json
	}
	
	@AfterTestCase
	def afterTestCase(TestCaseContext testCaseContext) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create()
		String json = gson.toJson(testCaseContext)
		Path afterTCReport = outDir.resolve('after_' + testCaseContext.getTestCaseId().replace('/', '_') + '.json')
		afterTCReport.toFile().text = json
		//
		memo.addTestCaseContext(testCaseContext)
	}
	
	@AfterTestSuite
	def afterTestSuite(TestSuiteContext testSuiteContext) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create()
		String json = gson.toJson(testSuiteContext)
		Path afterTSReport = outDir.resolve('after_' + testSuiteContext.getTestSuiteId().replace('/', '_') + '.json')
		afterTSReport.toFile().text = json
		//
		Path memoFile = outDir.resolve('memo_' + testSuiteContext.getTestSuiteId().replace('/', '_') + '.json')
		memoFile.toFile().text = memo.toJson()
	}
	
}