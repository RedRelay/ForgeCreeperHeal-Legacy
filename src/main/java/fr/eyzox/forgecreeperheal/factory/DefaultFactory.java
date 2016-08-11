package fr.eyzox.forgecreeperheal.factory;

import fr.eyzox.forgecreeperheal.factory.keybuilder.IKeyBuilder;

public class DefaultFactory<KEY, DATA extends IData<KEY>> extends Factory<KEY, DATA> {

	private final DATA _default;
	
	public DefaultFactory(final IKeyBuilder<KEY> keyBuilder, final DATA _default) {
		super(keyBuilder);
		this._default = _default;
	}
	
	@Override
	public DATA getData(String key) {
		DATA data = super.getData(key);
		if(data == null && _default.accept(keyBuilder.convertToKey(key))) {
			data = _default;
			customCache.put(key, data);
		}
		return data;
	}
	
	public DATA getDefault() {
		return _default;
	}

}
