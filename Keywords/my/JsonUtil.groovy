package my

import java.util.Comparator
import java.util.stream.Collectors

import com.google.gson.JsonElement
import com.google.gson.JsonObject

public class JsonUtil {

	static Comparator cm = { String a, String b -> a.compareToIgnoreCase(b) }
	
	private JsonUtil() {}

	/**
	 * Convert a JsonObject into another JsonObject of which keys are sorted alpha-numerically.
	 * 
	 * copied from
	 * https://stackoverflow.com/questions/30030601/how-to-create-json-sorted-on-keys-using-gson
	 * 
	 * @param jsonObject of Gson
	 * @return
	 */
	private static JsonObject sortAndGet(JsonObject jsonObject) {
		List<String> keySet = jsonObject.keySet().stream().sorted(cm).collect(Collectors.toList());
		JsonObject temp = new JsonObject();
		for (String key : keySet) {
			JsonElement ele = jsonObject.get(key);
			if (ele.isJsonObject()) {
				ele = sortAndGet(ele.getAsJsonObject());
				temp.add(key, ele);
			} else if (ele.isJsonArray()) {
				temp.add(key, ele.getAsJsonArray());
			} else
				temp.add(key, ele.getAsJsonPrimitive());
		}
		return temp;
	}
}
