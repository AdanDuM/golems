package com.golemgame.functional.component;

import com.golemgame.functional.component.medium.LightSource;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;


/**
 * A lights comes in two primary flavours - one that is a standard component and whose light
 * signal is controlled by its input; another that is solely a physical object, and is just 
 * registered with the light medium.
 * @author Sam
 *
 */
public abstract class BLight extends BComponent implements LightSource{
	
	
	private LightDataCallback lightData;
	
	public LightDataCallback getLightData() {
		return lightData;
	}

	public Vector3f getPosition()
	{
		return lightData.getPosition();
	}
	
	public Quaternion getRotation()
	{
		return lightData.getRotation();
	}
	
	public void setLightData(LightDataCallback lightData) {
		this.lightData = lightData;
	}

	public BLight(LightDataCallback lightData) {
		super();
		this.lightData = lightData;
	}

	/**
	 * This interface must be implemented and attached to the BLight to provide it with neccesary
	 * information about the position of the light, on request.
	 * @author Sam
	 *
	 */
	public static interface LightDataCallback
	{
		public Vector3f getPosition();
		public Quaternion getRotation();
		public void setIntensity(float intensity);
		public void setWaveLength(float wavelength);
	}
	
}
