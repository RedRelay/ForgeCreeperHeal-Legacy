package fr.eyzox.forgecreeperheal.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealReflectionException;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindMethodException;

public class ReflectionHelper {
	
	public static Object get(Field f, Object obj) {
		try {
			return f.get(obj);
		} catch (ReflectiveOperationException e) {
			throw new ForgeCreeperHealReflectionException(e);
		}
	}
	
	public static Object call(Object fromObject, Method method, Object... params) {
		try {
			return method.invoke(fromObject, params);
		} catch (ReflectiveOperationException e) {
			ForgeCreeperHeal.getLogger().error("Unable to call method "+method == null ? null : method.getName());
			throw new ForgeCreeperHealReflectionException(e);
		}
	}
	
	/**
	 * Replacement for net.minecraftforge.fml.relauncher.ReflectionHelper.findMethod(Class<? extends E>, E, String[], Class<?>[])
	 * @see https://github.com/MinecraftForge/MinecraftForge/issues/3676
	 */
	@Deprecated
	protected static Method findMethod(Class<?> clazz, String[] methodNames, Class<?>... methodTypes){
        Exception failed = null;
        for (String methodName : methodNames){
            try{
                Method m = clazz.getDeclaredMethod(methodName, methodTypes);
                m.setAccessible(true);
                return m;
            }
            catch (Exception e){
                failed = e;
            }
        }
        throw new UnableToFindMethodException(methodNames, failed);
    }
}
