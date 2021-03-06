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
package com.golemgame.structural.structures;

import java.util.Collection;
import java.util.Iterator;

import com.golemgame.mechanical.layers.Layer;
import com.golemgame.model.effect.ModelEffectPool;
import com.golemgame.mvc.PropertyStore;
import com.golemgame.properties.Property;
import com.golemgame.properties.Property.PropertyType;
import com.golemgame.views.Viewable;

public abstract class FunctionalStructure extends PhysicalStructure {

	private static final long serialVersionUID = 1L;

	public FunctionalStructure(PropertyStore store) {
		super(store);
		
	}

	@Override
	public Collection<Property> getPropertySet() {
		 Collection<Property> properties = super.getPropertySet();
		 Iterator<Property> it = properties.iterator();
		 while(it.hasNext())
		 {
			Property n = it.next();
			 if((n.getPropertyType()== PropertyType.MATERIAL) ||(n.getPropertyType()== PropertyType.PHYSICAL) || n.getPropertyType()==PropertyType.SOUND)
			 {
				 it.remove();
			 }
		 }

		return properties;
	}
	
	@Override
	public void refreshView() {
		/*output.refreshView();
		input.refreshView();*/
		Layer layer = getLayer();
	
		
		if (getViews().contains(Viewable.ViewMode.FUNCTIONAL))
		{
			this.getAppearance().addEffect(	this.getStructuralAppearanceEffect(),true);
			super.setSelectable(true);
			if(layer.isEditable())
			{
			
				if (super.getViews().contains(SELECTED))
				{
					this.getAppearance().addEffect(ModelEffectPool.getInstance().getSelectedEffect(), false);
				
				}else
				{
					this.getAppearance().removeEffect(ModelEffectPool.getInstance().getSelectedEffect(), false);
			
				}
			}else{
				this.getAppearance().removeEffect(ModelEffectPool.getInstance().getSelectedEffect(), false);
				//this.setSelectable(false); taken care of in selection info
			}
			
			if(layer.isVisible()){
				this.getAppearance().reapply();				
				this.getModel().setVisible(true);
			}else{
				this.getModel().setVisible(false);
			}
		}else
		{
			super.setSelectable(false);
			this.getModel().setVisible(false);
		}
		this.getAppearance().reapply();
		
		super.getViewManager().refreshView();
		
	}



}
