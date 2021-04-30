package com.kazurayam.ks.plainreport

import java.util.Comparator
import java.util.stream.Collectors

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

/**
 * 
 * @author kazurayam
 */
public class JsonSortUtil {

	static Comparator comparator = { String a, String b -> a.compareToIgnoreCase(b) }

	private JsonSortUtil() {}

	/**
	 * 
	 * @param jsonElement
	 * @return
	 */
	static JsonElement sort(JsonElement jsonElement) {
		if (jsonElement.isJsonObject()) {
			return sort(jsonElement.getAsJsonObject())
		} else if (jsonElement.isJsonArray()) {
			return sort(jsonElement.getAsJsonArray())
		} else if (jsonElement.isJsonNull()) {
			return jsonElement.getAsJsonNull()
		} else if (jsonElement.isJsonPrimitive()) {
			return jsonElement.getAsJsonPrimitive()
		} else {
			throw new IllegalArgumentException("given jsonElement is unknown type")
		}
	}

	static JsonObject sort(JsonObject jsonObject) {
		List<String> keySet = jsonObject.keySet().stream().sorted(comparator).collect(Collectors.toList())
		JsonObject sorted = new JsonObject()
		for (String key : keySet) {
			JsonElement ele = jsonObject.get(key)
			if (ele.isJsonObject()) {
				ele = sort(ele.getAsJsonObject())
				sorted.add(key, ele)
			} else if (ele.isJsonArray()) {
				sorted.add(key, sort(ele.getAsJsonArray()))
			} else if (ele.isJsonNull()) {
				sorted.add(key, ele.getAsJsonNull())
			} else if (ele.isJsonPrimitive()) {
				sorted.add(key, ele.getAsJsonPrimitive())
			} else {
				throw new IllegalArgumentException("given ele is unknown type")
			}
		}
		return sorted
	}

	static JsonArray sort(JsonArray jsonArray) {
		JsonArray result = new JsonArray()
		jsonArray.forEach { JsonElement je ->
			if (je.isJsonObject()) {
				JsonObject jo = je.getAsJsonObject()
				result.add(sort(jo))
			} else if (je.isJsonArray()) {
				JsonArray ja = je.getAsJsonArray()
				result.add(sort(ja))
			} else if (je.isJsonNull()) {
				result.add(je.getAsJsonNull())
			} else if (je.isJsonPrimitive()) {
				result.add(je.getAsJsonPrimitive())
			} else {
				throw new IllegalArgumentException("given je is unknown type")
			}
		}
		return result
	}
}
