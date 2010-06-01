package com.golemgame.functional.component;

public class BGenericSource extends BSource {
	
	private final String description;
	
	@Override
	public String toString() {		
		return description;
	}

	/**
	 * Values sent to this source are normalized to +/- 1 using this scale. (Anything larger becomes 1)
	 */
	private float normalizingMax = 1;
	
	/**
	 * Values sent to this source are normalized to +/- 1 using this scale. (Anything smaller becomes -1)
	 */
	private float normalizingMin = -1;
	
	private float observedData =0;
	
	/**
	 * If lasting observations is true, the sensor assumes that all observations remain valid until 
	 * new observations are set.
	 * If it is false(default), then the sensor assumes that observations return to 0 after reporting 
	 * the current observation.
	 */
	private boolean lastingObservations = false;
	/**
	 * If lasting observations is true, the sensor assumes that all observations remain valid until 
	 * new observations are set.
	 * If it is false(default), then the sensor assumes that observations return to 0 after reporting 
	 * the current observation.
	 */
	public boolean isLastingObservations() {
		return lastingObservations;
	}
	/**
	 * If lasting observations is true, the sensor assumes that all observations remain valid until 
	 * new observations are set.
	 * If it is false(default), then the sensor assumes that observations return to 0 after reporting 
	 * the current observation.
	 */
	public void setLastingObservations(boolean lastingObservations) {
		this.lastingObservations = lastingObservations;
	}

	public BGenericSource(String description) {
		super();
		this.description = description;
		this.setUpdatable(true);
		
	}

	@Override
	public void updateSource(float time) {
		
		state = observedData;
		if(!lastingObservations)
			observedData = 0;
		
	}
	
	/**
	 * Send and observation to this source. The value will be normalized to 1/-1, using the normalizing min and max as the range.
	 * @param observation
	 */
	public void setSensorObservation(float observation)
	{
		observedData = observation;
		
		float center = (normalizingMin+normalizingMax)/2f;
		
		observedData-= center;
		
		observedData/=(normalizingMax - center);
	
		if(observedData>1f)
			observedData=1f;
		else if(observedData<-1f)
			observedData=-1f;
	}

	public float getNormalizingMax() {
		return normalizingMax;
	}

	public void setNormalizingMax(float normalizingMax) {
		this.normalizingMax = normalizingMax;
	}

	public float getNormalizingMin() {
		return normalizingMin;
	}

	public void setNormalizingMin(float normalizingMin) {
		this.normalizingMin = normalizingMin;
	}
	
	
	
}
