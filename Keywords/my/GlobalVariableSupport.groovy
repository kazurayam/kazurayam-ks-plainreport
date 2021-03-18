package my

import java.lang.reflect.Field
import java.lang.reflect.Modifier

import internal.GlobalVariable

public class GlobalVariableSupport {

	/**
	 *
	 */
	static Map<String, Object> aquireGlobalVariablesAsMap() {
		
		Field[] fields = GlobalVariable.getClass().getDeclaredFields()
		
		List<Field> staticFields = new ArrayList<Field>()
		for (field in fields) {
			if (Modifier.isStatic(field.getModifiers())) {
				staticFields.add(field)
			}
		}
		Map<String, Object> globalVariables = new HashMap<String, Object>()
		for (field in staticFields) {
			Object value = field.get(GlobalVariable)
			globalVariables.put(field.name, value)
		}
	}
}

