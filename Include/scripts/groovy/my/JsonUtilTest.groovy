package my

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.google.gson.JsonObject
import com.google.gson.GsonBuilder

import com.google.gson.JsonParser

@RunWith(JUnit4.class)
class JsonUtilTest {

	@Before
	void setup() {
	}

	/**
	 */
	@Test
	void test_sortAndGet_smoke() {
		String input = """{
	"b": {
		"Y":"y",
		"X":"y"
		},
	"a": [3,2,1]
}
"""
		JsonObject inputJson = new JsonParser().parse(input).getAsJsonObject();
		String expected = """{
	"a": [3,2,1],
	"b": {
		"X":"y",
		"Y":"y"
		}
}"""
		JsonObject expectedJson = new JsonParser().parse(input).getAsJsonObject();
		JsonObject actualJson = JsonUtil.sortAndGet(inputJson)
		assertEquals(expectedJson, actualJson)
		println("actualJson is: " + actualJson)
	}
	
}
