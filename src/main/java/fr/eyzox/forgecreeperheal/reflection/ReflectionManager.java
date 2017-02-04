package fr.eyzox.forgecreeperheal.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealException;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealReflectionException;

public class ReflectionManager {

	private final static String MSG_UNREC_CLASS = "The %s class is not registered";
	
	private final static ReflectionManager INSTANCE = new ReflectionManager();
	
	private final Map<Class<?>, ReflectionClass> reflectClasses = new HashMap<Class<?>, ReflectionClass>();
	
	private ReflectionManager() {
	}
	
	public synchronized void registerField(Class<?> clazz, String name, String obfName) {
		getOrPut(clazz).registerField(name, obfName);
	}
	
	public synchronized void registerMethod(Class<?> clazz, String name, String obfName, Class<?>[] methodTypes) {
		getOrPut(clazz).registerMethod(name, obfName, methodTypes);
	}
	
	public synchronized void unregisterField(Class<?> clazz, String name) {
		ReflectionClass rClass = reflectClasses.get(clazz);
		if(rClass != null) {
			rClass.unregisterField(name);
			if(rClass.isEmpty()) {
				reflectClasses.remove(rClass.getClazz());
			}
		}
	}
	
	public synchronized void unregisterMethod(Class<?> clazz, String name, Class<?>[] methodTypes) {
		ReflectionClass rClass = reflectClasses.get(clazz);
		if(rClass != null) {
			rClass.unregisterMethod(name, methodTypes);
			if(rClass.isEmpty()) {
				reflectClasses.remove(rClass.getClazz());
			}
		}
	}
	
	public synchronized Field getField(Class<?> clazz, String name) {
		ReflectionClass rClass = reflectClasses.get(clazz);
		if(rClass == null) {
			throwUnrecClass(clazz);
		}
		return rClass.getField(name);
	}
	
	public synchronized Method getMethod(Class<?> clazz, String name, Class<?>[] methodTypes) {
		ReflectionClass rClass = reflectClasses.get(clazz);
		if(rClass == null) {
			throwUnrecClass(clazz);
		}
		return rClass.getMethod(name, methodTypes);
	}
	
	private ReflectionClass getOrPut(Class<?> clazz) {
		ReflectionClass rClass = reflectClasses.get(clazz);
		if(rClass == null) {
			rClass = new ReflectionClass(clazz);
			reflectClasses.put(rClass.getClazz(), rClass);
		}
		return rClass;
	}
	
	private void throwUnrecClass(Class<?> clazz) {
		final ForgeCreeperHealException e = new ForgeCreeperHealReflectionException(String.format(MSG_UNREC_CLASS,clazz.getName()));
		ForgeCreeperHeal.getLogger().error(e.getMessage());
		throw e;
	}
	
	public static ReflectionManager getInstance() {
		return INSTANCE;
	}
	
}
