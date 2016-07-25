package fr.eyzox.forgecreeperheal.serial;

import java.lang.reflect.Method;

import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealerSerialException;
import net.minecraft.nbt.NBTTagCompound;

public class SerialUtils {

	private static final String TAG_WRAPPER = "wrapper";
	private static final String TAG_WRAPPER_DATA = "data";
	
	private SerialUtils() {}
	
	public static <T> NBTTagCompound serializeWrappedData(ISerialWrapper<T> wrapper, T data) throws ForgeCreeperHealerSerialException {
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setString(TAG_WRAPPER, wrapper.getClass().getName());
		tag.setTag(TAG_WRAPPER_DATA, wrapper.serialize(data));
		return tag;
	}
	
	public static <T> T unserializeWrappedData(final NBTTagCompound tag) throws ForgeCreeperHealerSerialException {
		final String wrapperClassName = tag.getString(TAG_WRAPPER);
		
		if(wrapperClassName.isEmpty()) {
			throw new ForgeCreeperHealerSerialException("Missing SerialWrapper's class name");
		}
		
		ISerialWrapper<T> wrapper = null;
		try {
			Class<ISerialWrapper<T>> clazz = (Class<ISerialWrapper<T>>) Class.forName(wrapperClassName);
			Method getIntance = clazz.getMethod("getInstance", null);
			wrapper = (ISerialWrapper<T>) getIntance.invoke(null, null);
		}catch(ReflectiveOperationException e) {
			throw new ForgeCreeperHealerSerialException(e);
		}catch(ClassCastException e2) {
			throw new ForgeCreeperHealerSerialException(e2);
		}
		
		NBTTagCompound data = tag.getCompoundTag(TAG_WRAPPER_DATA);
		
		if(data.hasNoTags()) {
			throw new ForgeCreeperHealerSerialException("Missing wrapper's data");
		}
		
		return wrapper.unserialize(data);
	}
	
}
