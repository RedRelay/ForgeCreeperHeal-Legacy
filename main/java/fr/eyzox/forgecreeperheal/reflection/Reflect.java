package fr.eyzox.forgecreeperheal.reflection;

import java.lang.reflect.Field;

public class Reflect {

	public static Field getFieldForClass(Class<?> clazz, String deobf, String obf){
		Field f = null;
		try {
			f = clazz.getDeclaredField(obf);
		} catch (ReflectiveOperationException e) {
			try {
				f = clazz.getDeclaredField(deobf);
			} catch (ReflectiveOperationException e1) {
				throw new RuntimeException(e1);
			}
		}
		f.setAccessible(true);
		return f;
	}
	
	public static Object getDataFromField(Field f, Object obj) {
		Object o = null;
		try {
			o = f.get(obj);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		return o;
	}
	
}
