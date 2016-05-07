package fr.eyzox.forgecreeperheal.factory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.eyzox.forgecreeperheal.factory.keybuilder.IKeyBuilder;

public class Factory<KEY, DATA extends IData<KEY>> {

	protected final List<DATA> custom = new LinkedList<DATA>();
	protected final Map<String, DATA> customCache = new HashMap<String, DATA>();
	
	protected final IKeyBuilder<KEY> keyBuilder;
	
	public Factory(final IKeyBuilder<KEY> keyBuilder) {
		this.keyBuilder = keyBuilder;
	}
	
	public List<DATA> getCustomHandlers() {
		return custom;
	}
	
	public DATA getData(final KEY key) {
		return this.getData(keyBuilder.convertToString(key));
	}
	
	public DATA getData(final String key) {
		DATA data = customCache.get(key);
		if(data == null) {
			for(DATA dataFromCustom : custom) {
				if(dataFromCustom.accept(keyBuilder.convertToKey(key))) {
					data = dataFromCustom;
					break;
				}
			}
			
			if(data != null){
				customCache.put(key, data);
			}
			
		}

		return data;
	}
	
	public void clearCache() {
		customCache.clear();
	}

}
