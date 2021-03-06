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
package com.golemgame.mechanical;

import java.io.Serializable;

import com.jmex.buoyancy.IFluidRegion;

public class BuoyancySettings  implements Serializable{

	private static final long serialVersionUID = 1L;

	private boolean enabled = false;
	private float fluidDensity = IFluidRegion.DENSITY_WATER;
	private float fluidViscotiy = IFluidRegion.VISCOSITY_WATER;
	private float fluidHeight = 0;
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public float getFluidDensity() {
		return fluidDensity;
	}
	public void setFluidDensity(float fluidDensity) {
		this.fluidDensity = fluidDensity;
	}
	public float getFluidViscotiy() {
		return fluidViscotiy;
	}
	public void setFluidViscotiy(float fluidViscotiy) {
		this.fluidViscotiy = fluidViscotiy;
	}
	public float getFluidHeight() {
		return fluidHeight;
	}
	public void setFluidHeight(float fluidHeight) {
		this.fluidHeight = fluidHeight;
	}
	
	

	public BuoyancySettings() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
