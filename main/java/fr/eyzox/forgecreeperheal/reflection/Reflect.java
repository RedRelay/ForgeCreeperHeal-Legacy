package fr.eyzox.forgecreeperheal.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;

public class Reflect {

	public static Field getFieldForClass(Class<?> clazz, String deobf, String obf){
		Field[] fields = clazz.getDeclaredFields();
		for(Field f : fields) {
			if(f.getName().equals(deobf) || f.getName().equals(obf)) {
				f.setAccessible(true);
				return f;
			}
		}
		
		final String errorMsg = "Unable to find field named "+deobf+" or "+obf;
		ForgeCreeperHeal.getLogger().error(errorMsg);
		throw new RuntimeException(errorMsg);
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
	
	public static Method getMethodForClass(Class<?> clazz, String deobf, String obf) {
		Method[] methods = clazz.getDeclaredMethods();
		for(Method m : methods) {
			if(m.getName().equals(deobf) || m.getName().equals(obf)) {
				m.setAccessible(true);
				return m;
			}
		}
		final String errorMsg = "Unable to find method named "+deobf+" or "+obf;
		ForgeCreeperHeal.getLogger().error(errorMsg);
		throw new RuntimeException(errorMsg);
	}
	
	public static Object call(Object fromObject, Method method, Object... params) {
		try {
			return method.invoke(fromObject, params);
		} catch (Exception e) {
			ForgeCreeperHeal.getLogger().error("Unable to call method "+method == null ? null : method.getName());
			throw new RuntimeException(ForgeCreeperHeal.MODNAME, e);
		}
	}
}
