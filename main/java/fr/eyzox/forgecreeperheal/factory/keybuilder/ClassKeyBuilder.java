package fr.eyzox.forgecreeperheal.factory.keybuilder;

public class ClassKeyBuilder<E> implements IKeyBuilder<Class<? extends E>> {
	
	@Override
	public String convertToString(Class<? extends E> key) {
		return key.getCanonicalName();
	}

	@Override
	public Class<? extends E> convertToKey(String keyStr) {
		try {
			return (Class<? extends E>) Class.forName(keyStr);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
			//TODO serial exception
		}
	}

}
