package com.golemgame.mvc.golems.validate;

import com.golemgame.mvc.BoolType;
import com.golemgame.mvc.golems.SphereInterpreter;
import com.golemgame.mvc.golems.validate.requirement.StrictDataTypeRequirement;
import com.golemgame.mvc.golems.validate.requirement.Vector3MinimumRequirement;
import com.jme.math.Vector3f;

public class ContactValidator extends PhysicalValidator {


	public ContactValidator() {
		super();
		super.requireData(SphereInterpreter.RADII, new Vector3f(0.5f,0.5f,0.5f));
		//super.requireData(SphereInterpreter.ELLIPSOID, false);
		super.addRequirement(new StrictDataTypeRequirement(SphereInterpreter.ELLIPSOID,new BoolType(false)) );
		super.addRequirement( new Vector3MinimumRequirement(MIN_EXTENT,MIN_EXTENT,MIN_EXTENT,SphereInterpreter.RADII));
		
	}


}
