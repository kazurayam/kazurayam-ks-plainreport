package my

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.driver.DriverType

public class Memo {

	private TestSuiteContext testSuite
	private String executionProfile
	private String executedBrowser
	private List<TestCaseContext> testCases
	private Map<String, Object> executionProperties

	Memo() {
		this(null)
	}

	Memo(TestSuiteContext testSuiteContext) {
		this.testSuite = testSuiteContext
		this.testCases = new ArrayList<TestCaseContext>()
	}

	void setExecutionProfile(String executionProfile) {
		this.executionProfile =  executionProfile
	}
	
	void setExecutedBrowser(String executedBrowser) {
		this.executedBrowser = executedBrowser
	}

	void addTestCaseContext(TestCaseContext testCaseContext) {
		testCases.add(testCaseContext)
	}

	void setExecutionProperties(Map map) {
		this.executionProperties = map
	}

	String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create()
		return gson.toJson(this)
	}
}
