package com.kazurayam.ks.plainreport

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Ignore
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.GsonBuilder

import com.google.gson.JsonParser

@RunWith(JUnit4.class)
class JsonSortUtilTest {

	String input = """{
	"e": "e",
	"d": null,
	"c": 1,
	"b": {
		"Y":"y",
		"X":"y"
		},
	"a": [{"L":1,"K":0},{"G":3,"F":2}],
	"E": "E"
}
"""
	String expected = """{
	"a": [{"K":0,"L":1},{"F":2,"G":3}],
	"b": {
		"X":"y",
		"Y":"y"
		},
	"c": 1,
	"d": null,
	"e": "e",
	"E": "E"
}"""

	@Before
	void setup() {
	}


	@Test
	void test_comparator() {
		String var2 = "var2"
		String var1 = "var1"
		int result = JsonSortUtil.comparator.compare(var2, var1)
		assertEquals(1, result)
	}

	@Test
	void test_sort() {
		JsonElement inputJson = new JsonParser().parse(input)
		JsonElement expectedJson = new JsonParser().parse(expected)
		JsonElement actualJson = JsonSortUtil.sort(inputJson)
		assertEquals(expectedJson, actualJson)
		println("actualJson is: " + actualJson)
	}
}
