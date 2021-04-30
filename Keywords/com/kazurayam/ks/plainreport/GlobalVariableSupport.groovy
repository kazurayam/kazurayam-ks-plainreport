package com.kazurayam.ks.plainreport

import java.lang.reflect.Field
import java.lang.reflect.Modifier

import internal.GlobalVariable

public class GlobalVariableSupport {

	/**
	 *
	 */
	static Map<String, Object> aquireGlobalVariablesAsMap() {
		Field[] declaredFields = GlobalVariable.class.getDeclaredFields()
		//println "declaredFields.size() is: ${declaredFields.size()}"
		List<Field> pubstaticFields = new ArrayList<Field>()
		for (Field declaredField in declaredFields) {
			//println "declaredField.getName() is: ${declaredField.getName()}"
			if (Modifier.isStatic(declaredField.getModifiers()) &&
				Modifier.isPublic(declaredField.getModifiers()) &&
				! declaredField.getName().startsWith("__")  ) {
				pubstaticFields.add(declaredField)
			}
		}
		//println "pubstaticFields.size() is: ${pubstaticFields.size()}"
		Map<String, Object> globalVariables = new HashMap<String, Object>()
		for (field in pubstaticFields) {
			Object value = field.get(null)
			//println "field.getName() is: ${field.getName()}"
			globalVariables.put(field.getName(), value)
		}
		return globalVariables
	}
}

