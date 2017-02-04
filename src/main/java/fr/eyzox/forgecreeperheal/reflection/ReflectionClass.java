package fr.eyzox.forgecreeperheal.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealException;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealReflectionException;

class ReflectionClass {
	
	private static class MethodKey {
		
		private final static Class<?>[] NO_METHOD_TYPE = new Class<?>[0];
		
		private final String name;
		private final Class<?>[] methodTypes;
		
		private MethodKey(String name, Class<?>[] methodTypes) {
			this.name = name;
			this.methodTypes = methodTypes == null ? NO_METHOD_TYPE : methodTypes;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(methodTypes);
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MethodKey other = (MethodKey) obj;
			if (!Arrays.equals(methodTypes, other.methodTypes))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder(name);
			sb.append('(');
			for(int i=0; i<methodTypes.length; i++) {
				sb.append(methodTypes[i].getName());
				if(i < methodTypes.length-1) {
					sb.append(", ");
				}
			}
			sb.append(')');
			return sb.toString();
		}
		
	}
	
	private final Class<?> clazz;
	private final Map<String, Field> fields = new HashMap<String, Field>();
	private final Map<MethodKey, Method> methods = new HashMap<MethodKey, Method>(); 
	
	protected ReflectionClass(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
	
	public synchronized Field getField(String name) {
		final Field f = fields.get(name);
		if(f == null) {
			final ForgeCreeperHealException e = new ForgeCreeperHealReflectionException(String.format("The %s.%s field is not registered",clazz.getName(), name));
			ForgeCreeperHeal.getLogger().error(e.getMessage());
			throw e;
		}
		return f;
	}
	
	public synchronized Method getMethod(String name, Class<?>[] methodTypes) {
		final MethodKey key = new MethodKey(name, methodTypes);
		final Method m = methods.get(key);
		if(m == null) {
			final ForgeCreeperHealException e = new ForgeCreeperHealReflectionException(String.format("The %s.%s method is not registered",clazz.getName(), key.toString()));
			ForgeCreeperHeal.getLogger().error(e.getMessage());
			throw e;
		}
		return m;
	}
	
	protected synchronized void registerField(String name, String obfName) {
		fields.put(name, net.minecraftforge.fml.relauncher.ReflectionHelper.findField(this.clazz, obfName, name));
	}
	
	protected synchronized void registerMethod(String name, String obfName, Class<?>[] methodTypes) {
		final MethodKey key = new MethodKey(name, methodTypes);
		methods.put(key, ReflectionHelper.findMethod(this.clazz, new String[]{obfName, name}, methodTypes));
	}
	
	protected synchronized void unregisterField(String name) {
		fields.remove(name);
	}
	
	protected synchronized void unregisterMethod(String name, Class<?>[] methodTypes) {
		final MethodKey key = new MethodKey(name, methodTypes);
		methods.remove(key);
	}
	
	protected synchronized boolean isEmpty() {
		return fields.isEmpty() && methods.isEmpty();
	}
}
