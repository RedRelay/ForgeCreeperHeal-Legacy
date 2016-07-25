package fr.eyzox.dependencygraph.interfaces;

import fr.eyzox.dependencygraph.DataKeyProvider;

public interface IData<KEY> {
	public DataKeyProvider<KEY> getDataKeyProvider();
}
