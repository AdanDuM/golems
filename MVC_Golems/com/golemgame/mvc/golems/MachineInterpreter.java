package com.golemgame.mvc.golems;

import java.util.Collection;

import com.golemgame.mvc.CollectionType;
import com.golemgame.mvc.DataType;
import com.golemgame.mvc.PropertyStore;

public class MachineInterpreter extends SpatialInterpreter {
	
	public final static String STRUCTURES = "machine.structures";
	public final static String GROUPS = "groups";
	public final static String LAYER_REPOSITORY = "layers";
	//public final static String WIRES = "machine.wires";
	
	public MachineInterpreter(PropertyStore store) {
		super(store);
		store.setClassName(GolemsClassRepository.MACHINE_CLASS);
		
	}
	@Override
	public Collection<String> enumerateKeys(Collection<String> keys) {
		keys.add(STRUCTURES);
		keys.add(GROUPS);
		keys.add(LAYER_REPOSITORY);
	//	keys.add(WIRES);
		return super.enumerateKeys(keys);
	}
	
	@Override
	public DataType getDefaultValue(String key) {
		if(key.equals(STRUCTURES))
			return defaultCollection;
		if(key.equals(GROUPS))
			return defaultStore;
	/*	if(key.equals(WIRES))
			return defaultStore;*/
		if(key.equals(LAYER_REPOSITORY))
			return defaultStore;

		return super.getDefaultValue(key);
	}
	
	/*public PropertyStore getWires()
	{
		return getStore().getPropertyStore(WIRES);
	}*/

	public PropertyStore getGroups()
	{
		return getStore().getPropertyStore(GROUPS);
	}
	
	public CollectionType getStructures()
	{
		return getStore().getCollectionType(STRUCTURES);
	}
	
	public void addStructure(PropertyStore structure)
	{
		this.getStructures().addElement(structure);
	}
	
	public void removeStructure(PropertyStore structure)
	{
		this.getStructures().removeElement(structure);
	}


	public MachineInterpreter() {
		this(new PropertyStore());		
	}
	public PropertyStore getLayerRepository() {
		return getStore().getPropertyStore(LAYER_REPOSITORY); 
	}
	
	
	
}
