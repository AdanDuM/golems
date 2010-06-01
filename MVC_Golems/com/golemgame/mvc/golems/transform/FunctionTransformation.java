package com.golemgame.mvc.golems.transform;

import com.golemgame.mvc.DataType;
import com.golemgame.mvc.EnumType;
import com.golemgame.mvc.PropertyStore;
import com.golemgame.mvc.golems.BatteryInterpreter;
import com.golemgame.mvc.golems.GolemsClassRepository;
import com.golemgame.mvc.golems.MachineInterpreter;
import com.golemgame.mvc.golems.MachineSpaceInterpreter;
import com.golemgame.mvc.golems.ModifierInterpreter;
import com.golemgame.mvc.golems.functions.FunctionSettingsInterpreter;

public class FunctionTransformation extends Transformation {
	
	public FunctionTransformation() {
		super("0.53.7");		
	}
	
	@Override
	public void apply(PropertyStore store) {
		//take the machine space, and find any battery structures, and update their function stores
		
		MachineSpaceInterpreter machineSpace = new MachineSpaceInterpreter(store);
		for(DataType val: machineSpace.getMachines().getValues())
		{
			if(val instanceof PropertyStore)
			{
			
				apply( new MachineInterpreter((PropertyStore) val));
				
				
			}
		}
		
		
	}
	
	private void apply(MachineInterpreter interpreter)
	{
		for(DataType val: interpreter.getStructures().getValues())
		{
			if(val instanceof PropertyStore)
			{
				 
				PropertyStore store = (PropertyStore) val;
				if(GolemsClassRepository.BATTERY_CLASS.equals( ((PropertyStore) val).getClassName()))
				{
					apply(new BatteryInterpreter(store));
				}else if(GolemsClassRepository.MODIFIER_CLASS.equals( ((PropertyStore) val).getClassName()))
				{
					apply(new ModifierInterpreter(store));
				}	
				
			}
		}
		
	}

	private void apply(ModifierInterpreter interpreter) {

		
		PropertyStore oldFunction = null;
		FunctionSettingsInterpreter oldSettings = null;
		EnumType functionType = null;
		
		if (interpreter.getStore().hasProperty(FUNCTION,DataType.Type.PROPERTIES))
		{
			oldFunction = interpreter.getStore().getPropertyStore(FUNCTION);
			interpreter.getStore().nullifyKey(FUNCTION);
		}
		
		if (interpreter.getStore().hasProperty(FUNCTION_TYPE,DataType.Type.ENUM))
		{
			DataType d = interpreter.getStore().getProperty(FUNCTION_TYPE);
			if(d instanceof EnumType)
				functionType = (EnumType)d;
			interpreter.getStore().nullifyKey(FUNCTION_TYPE);
		}
		
		oldSettings = new FunctionSettingsInterpreter( interpreter.getStore().getPropertyStore(ModifierInterpreter.FUNCTION_SETTINGS));
		
		if(oldFunction != null )
		{
			oldSettings.setFunction(oldFunction);		
			
		}
		if(functionType != null)
		{
			oldSettings.getStore().setProperty(FunctionSettingsInterpreter.FUNCTION_TYPE, functionType);
		}
	}
	
	//this is DEPRECATED!
	private static final String FUNCTION = "function";
	private static final String FUNCTION_TYPE = "function.type";
	
	private void apply(BatteryInterpreter interpreter) {

		
		PropertyStore oldFunction = null;
		FunctionSettingsInterpreter oldSettings = null;
		EnumType functionType = null;
		
		if (interpreter.getStore().hasProperty(FUNCTION,DataType.Type.PROPERTIES))
		{
			oldFunction = interpreter.getStore().getPropertyStore(FUNCTION);
			interpreter.getStore().nullifyKey(FUNCTION);
		}
		
		if (interpreter.getStore().hasProperty(FUNCTION_TYPE,DataType.Type.ENUM))
		{
			DataType d = interpreter.getStore().getProperty(FUNCTION_TYPE);
			if(d instanceof EnumType)
				functionType = (EnumType)d;
			interpreter.getStore().nullifyKey(FUNCTION_TYPE);
		}
		
		oldSettings = new FunctionSettingsInterpreter( interpreter.getStore().getPropertyStore(BatteryInterpreter.FUNCTION_SETTINGS));
		
		if(oldFunction != null )
		{
			oldSettings.setFunction(oldFunction);		
		}
		if(functionType != null)
		{
			oldSettings.getStore().setProperty(FunctionSettingsInterpreter.FUNCTION_TYPE, functionType);
		}
	}

}
