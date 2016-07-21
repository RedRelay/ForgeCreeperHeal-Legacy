package fr.eyzox.forgecreeperheal.serial;

import java.lang.reflect.Method;

import net.minecraft.nbt.NBTTagCompound;

public class SerialUtils {

	private static final String TAG_WRAPPER = "wrapper";
	private static final String TAG_WRAPPER_DATA = "data";
	
	private SerialUtils() {}
	
	public static <T> NBTTagCompound serializeWrappedData(ISerialWrapperProvider<T> wrapper, T data) {
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setString(TAG_WRAPPER, wrapper.getSerialWrapper().getClass().getName());
		tag.setTag(TAG_WRAPPER_DATA, wrapper.getSerialWrapper().serialize(data));
		return tag;
	}
	
	public static <T> T unserializeWrappedData(final NBTTagCompound tag) throws ReflectiveOperationException {
		final String wrapperClassName = tag.getString(TAG_WRAPPER);
		Class<ISerialWrapper<T>> clazz = (Class<ISerialWrapper<T>>) Class.forName(wrapperClassName);
		Method getIntance = clazz.getMethod("getInstance", null);
		ISerialWrapper<T> wrapper = (ISerialWrapper<T>) getIntance.invoke(null, null);
		return wrapper.unserialize(tag.getCompoundTag(TAG_WRAPPER_DATA));
	}
	
}
