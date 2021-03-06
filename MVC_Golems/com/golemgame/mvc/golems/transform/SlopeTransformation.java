/*******************************************************************************
 * Copyright 2008, 2009, 2010 Sam Bayless.
 * 
 *     This file is part of Golems.
 * 
 *     Golems is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     Golems is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with Golems. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.golemgame.mvc.golems.transform;

import com.golemgame.mvc.DataType;
import com.golemgame.mvc.PropertyStore;
import com.golemgame.mvc.golems.GolemsClassRepository;
import com.golemgame.mvc.golems.MachineInterpreter;
import com.golemgame.mvc.golems.MachineSpaceInterpreter;
import com.golemgame.mvc.golems.ModifierInterpreter;
import com.golemgame.mvc.golems.functions.FunctionSettingsInterpreter;
import com.golemgame.mvc.golems.functions.FunctionSettingsInterpreter.FunctionType;

/**
 * Fix slope functions that incorrectly were counted as polynomial functions
 * @author Sam
 *
 */
public class SlopeTransformation extends Transformation {

	public SlopeTransformation() {
		super("0.53.11");		
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
				if(GolemsClassRepository.MODIFIER_CLASS.equals( ((PropertyStore) val).getClassName()))
				{
					apply(new ModifierInterpreter(store));
				}	

			}
		}

	}

	private void apply(ModifierInterpreter interpreter) {


		
		if(interpreter.getStore().hasProperty(ModifierInterpreter.FUNCTION_SETTINGS))
		{
			FunctionSettingsInterpreter interp = new FunctionSettingsInterpreter( interpreter.getFunctionStore());
			if(interp.getFunctionType() == FunctionType.Function)
			{
				if(interp.getFunctionName().equals("(Slope)"))//in exactly this case, previous versions might have failed to set this as a differentiator.
				{
					interp.setFunctionType(FunctionType.Differentiate);
				}
			}
		}

	

	}

	//this is DEPRECATED!
	private static final String FUNCTION = "function";
	private static final String FUNCTION_TYPE = "function.type";

}


