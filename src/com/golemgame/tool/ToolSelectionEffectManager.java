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
package com.golemgame.tool;

import com.golemgame.states.StateManager;


public class ToolSelectionEffectManager {
	private final static ToolSelectionEffectManager instance = new ToolSelectionEffectManager();

	private ToolSelectionEffect currentEffect = null;
	
	private ToolSelectionEffectManager() {
		super();
	}

	public static ToolSelectionEffectManager getInstance() {
		return instance;
	}
	
	public ToolSelectionEffect getCurrentEffect() {
		return currentEffect;
	}
	public void forceDisengage()
	{
		setCurrentEffect(null);
	}
	public void setCurrentEffect(ToolSelectionEffect effect)
	{
		if(currentEffect!=null && currentEffect != effect)
			currentEffect.forceDisengage();
		
		ToolSelectionEffect oldEffect = currentEffect;
		currentEffect= effect;

		if(currentEffect!=null  )
		{
			
			StateManager.getRootModel().addChild(effect.getModel());
/*			if(oldEffect!=null )
			{
				currentEffect.getModel().setModelData(oldEffect.getModel());
			}*/
			
			
			
		}
	}
	
	
	
}
