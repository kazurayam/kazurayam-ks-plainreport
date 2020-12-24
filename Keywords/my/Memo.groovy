package my

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext

public class Memo {

	private TestSuiteContext testSuite
	private String executionProfile
	private List<TestCaseContext> testCases

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

	void addTestCaseContext(TestCaseContext testCaseContext) {
		testCases.add(testCaseContext)
	}

	String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create()
		return gson.toJson(this)
	}
}
