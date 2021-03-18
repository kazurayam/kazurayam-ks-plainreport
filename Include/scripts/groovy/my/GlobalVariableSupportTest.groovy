package my

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4.class)
public class GlobalVariableSupportTest {

	@Before
	void setup() {}

	@Test
	void test_smoke() {
		Map<String, Object> globalVariables = GlobalVariableSupport.aquireGlobalVariablesAsMap()
		assert globalVariables != null
		assertTrue("globalVariables does not have key foo", globalVariables.containsKey("foo"))
		assertEquals("bar", globalVariables["foo"])
	}
}
