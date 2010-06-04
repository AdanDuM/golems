package com.golemgame.properties.fengGUI;

import org.fenggui.Button;
import org.fenggui.Container;
import org.fenggui.FengGUI;
import org.fenggui.TextEditor;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.layout.BorderLayout;
import org.fenggui.layout.BorderLayoutData;
import org.fenggui.layout.GridLayout;

import com.golemgame.local.StringConstants;
import com.golemgame.model.Model;
import com.golemgame.mvc.PropertyStore;
import com.golemgame.mvc.golems.CylinderInterpreter;
import com.golemgame.mvc.golems.GearInterpreter;
import com.golemgame.mvc.golems.GolemsClassRepository;
import com.golemgame.mvc.golems.RackGearInterpreter;
import com.golemgame.states.StateManager;
import com.golemgame.tool.SelectionListener;
import com.golemgame.tool.action.Action;
import com.golemgame.tool.action.ActionTypeException;
import com.golemgame.tool.action.Actionable;
import com.golemgame.tool.action.UndoManager;
import com.golemgame.tool.action.information.PropertyStoreInformation;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public class GearTab extends PropertyTabAdapter {

	protected static final int MAX_ITERATIONS = 1024*8;

	//private Collection<PropertyStore> properties = new ArrayList<PropertyStore>();

	private GearInterpreter interpreter;
	
	//private TextEditor toothNumber;
	private TextEditor toothHeight;
	private TextEditor toothAngle;
	private TextEditor toothWidth;
	private Button match;
	
	private float newRadius=-1f;
	private Quaternion newRotation=null;
	
	public GearTab() {
		super(StringConstants.get("TOOLBAR.GEAR","Gear"));
		
	}

	@Override
	protected void buildGUI() {
		
		getTab().setLayoutManager(new BorderLayout());
		
		Container toothContainer = FengGUI.createContainer(getTab());
		toothContainer.setLayoutData(BorderLayoutData.NORTH);
		toothContainer.setLayoutManager(new GridLayout(4,2));
	/*	FengGUI.createLabel(toothContainer, "Number of Teeth");
		toothNumber = FengGUI.createTextEditor();*/
		
		FengGUI.createLabel(toothContainer, StringConstants.get("PROPERTIES.GEAR.TOOTH_WIDTH","Tooth Width"));
		toothWidth = FengGUI.createTextEditor(toothContainer);
		
		FengGUI.createLabel(toothContainer, StringConstants.get("PROPERTIES.GEAR.TOOTH_HEIGHT","Tooth Height"));
		toothHeight = FengGUI.createTextEditor(toothContainer);
		
		FengGUI.createLabel(toothContainer, StringConstants.get("PROPERTIES.GEAR.TOOTH_ANGLE","Tooth Angle"));
		toothAngle = FengGUI.createTextEditor(toothContainer);
		
		match = FengGUI.createButton(toothContainer,StringConstants.get("PROPERTIES.GEAR.MATCH", "Match Gear"));
		FengGUI.createLabel(toothContainer,StringConstants.get("PROPERTIES.GEAR.MATCH.DESCRIPTION", "Select a gear to copy properties from so they can mesh properly."));

		match.addButtonPressedListener(new IButtonPressedListener(){

			public void buttonPressed(ButtonPressedEvent e) {
				if(!StateManager.getToolManager().hasListener(matchListener))
				{
					StateManager.getToolManager().attachListener(matchListener);
				}
			}
			
		});

	}

	
	private SelectionListener matchListener = new SelectionListener(){

		public boolean select(Actionable actionable, Model model) {
			
			try
			{
				PropertyStoreInformation info = (PropertyStoreInformation) actionable.getAction(Action.PROPERTY_STORE);
				PropertyStore store = info.getStore();			
				if(store!=null)
/*						&& store.hasProperty(GearInterpreter.TOOTH_ANGLE, Type.FLOAT)
						&& store.hasProperty(GearInterpreter.TOOTH_WIDTH, Type.FLOAT)			
						&& store.hasProperty(GearInterpreter.TOOTH_HEIGHT, Type.FLOAT))		*/		
				{
					if(store.getClassName().equalsIgnoreCase(GolemsClassRepository.GEAR_CLASS))
					{//its a circular gear:
						
						float width = store.getFloat(GearInterpreter.TOOTH_WIDTH);
						float height = store.getFloat(GearInterpreter.TOOTH_HEIGHT);
						float angle = store.getFloat(GearInterpreter.TOOTH_ANGLE);
						float otherRadius = store.getFloat(CylinderInterpreter.CYL_RADIUS);
					
						
						//ok: Gear to Gear: Match the width and height of the other gear,
						//then adjust the radius of this cylinder so that the radians per tooth are equal
						toothWidth.setText(String.valueOf(width));
						toothHeight.setText(String.valueOf(height));
						toothAngle.setText(String.valueOf(fromRadians(angle) ));
						
						//radians per tooth = 2PI/numberOfTeeth
						
						double oldWidth = interpreter.getToothWidth();
						double oldAngle = interpreter.getToothAngle();
						double oldHeight = interpreter.getToothHeight();
						
						double fullWidth =  oldWidth*2.0 + (float)Math.tan(oldAngle/2.0)*oldHeight*2.0;
						double fullWidthOther =  width*2.0 + (float)Math.tan(angle/2.0)*height*2.0;;
						double radiusLocal = interpreter.getStore().getFloat(CylinderInterpreter.CYL_RADIUS);
						double numberOfTeethLocal =(int)Math.floor( 2.0*Math.PI*radiusLocal/fullWidth);//interpreter.getNumberOfTeeth();
						
						double numberOfTeethOther =(int)Math.floor( 2.0*Math.PI*otherRadius/fullWidthOther);//interpreter.getNumberOfTeeth();
						
						double radiansPerToothLocal = FastMath.TWO_PI/numberOfTeethLocal;
						double radiansPerToothOther = FastMath.TWO_PI/numberOfTeethOther;
						
						double arcPerTooth = radiansPerToothOther*otherRadius;
						double arcPerToothLocal = radiansPerToothLocal*radiusLocal;
						//ok, now set the radius of this cylinder such that the arc per tooth will be equal
						newRadius =(float)( arcPerTooth/radiansPerToothLocal);
						
						//now do a check: sometimes its not possible to match the other's arc, and in this case repeated applications of match will
						//just repeatedly increase the radius and number of teeth.
						
						double newNumberOfTeeth=(int)Math.floor( 2.0*Math.PI*newRadius/fullWidth);
						if(newNumberOfTeeth>numberOfTeethLocal)
							newRadius = -1;
						
						Quaternion otherRotation = store.getQuaternion(CylinderInterpreter.LOCALROTATION);
						
						newRotation = otherRotation;
					//	newRotation = new Quaternion(otherRotation).multLocal(new Quaternion().fromAngleNormalAxis((float)radiansPerToothLocal/2f, Vector3f.UNIT_X));
						
					}else if( store.getClassName().equalsIgnoreCase(GolemsClassRepository.RACK_GEAR_CLASS))
					{//its a rack gear
						//just match that gears spacing
						
					
						//have to adjust tooth WIDTH and cylinder RADIUS so that the intertooth DISTANCE (not arc) = the rack tooth width
						//the problem is that this solution is underdetermined. 
						//so, lets pick the gear that has an intertooth distance EQUAL to its tooth width,
						//and with a tooth width equal to the rack tooth width
						
						
						double width = store.getFloat(RackGearInterpreter.TOOTH_WIDTH);
						double height = store.getFloat(RackGearInterpreter.TOOTH_HEIGHT);
						double angle = store.getFloat(RackGearInterpreter.TOOTH_ANGLE);
					
						double radius = interpreter.getStore().getFloat(CylinderInterpreter.CYL_RADIUS);
						
						double fullWidth =  width*2.0 + (float)Math.tan(angle/2.0)*height*2.0;
					
						double numberOfTeeth =(int)Math.floor( 2.0*Math.PI*radius/fullWidth);//interpreter.getNumberOfTeeth();

						double radiansPerTooth = FastMath.TWO_PI/numberOfTeeth;
						
						
						//the total arc per tooth, measured at the radius (not radius + height)
						//totalArcPerTooth =  2.0*radiansPerToothWidth*r+radiansPerToothAngle*2.0*r;
						//find r, given that we know the straight (not arc) width of the tooth and intertooth distance.
						//that is, we know the chord (= radius*2sin(theta/2)) of the tooth and intertooth distances.
						
						
					
					
						int its = 0;
						double dif = 1;
						double lastGreater = radius;
						double lastWidth = 0;
						//do a binary search for the best fit.
						
						double smaller = 0;
						double larger = radius*100.0;
						double nRadius =( smaller + larger)/2.0;
						while (its++<MAX_ITERATIONS)
						{							
							double radiansPerToothWidth = 2.0*Math.asin(width/(2.0*nRadius));

							double R = (float) Math.sqrt( (nRadius + height)*(nRadius + height) + width*width/4.0) ;
													
							//equation for the line of the gear edge
							double m = Math.tan(angle/2.0);
							double b = m*R;//y intersect is slope times BIG R.
							
							double xIntersect = quadraticFormula(m*m+1.0,2.0*m*b,b*b-nRadius*nRadius);
							if(Double.isNaN(xIntersect))
								xIntersect = 0.0;				
							double radiansPerToothAngle = Math.abs(Math.acos(Math.abs(xIntersect)/nRadius));
							
							double radiansPerToothCur = radiansPerToothAngle*2.0 + radiansPerToothWidth*2.0;
							
							//, now find the unrounded number of teeth that can fit in this new radius, using this radians per tooth
							 
							double curDif = radiansPerTooth - radiansPerToothCur;
						
							
							if(curDif>=0 && curDif<dif)
							{
								lastGreater = nRadius;
								nRadius = nRadius * 0.9999;
								dif = curDif;
								lastWidth = radiansPerToothWidth;
							}else{
								System.out.println("Breka");
								break;
							}
							
						}
						System.out.println(its + "\t" + MAX_ITERATIONS);
						double R = (float) Math.sqrt( (lastGreater + height)*(lastGreater + height) + width*width/4.0) ;
						
						//equation for the line of the gear edge
						double m = Math.tan(angle/2.0);
						double b = m*R;//y intersect is slope times BIG R.
						double xIntersect = quadraticFormula(m*m+1.0,2.0*m*b,b*b-lastGreater*lastGreater);
						if(Double.isNaN(xIntersect))
							xIntersect = 0.0;	

						double radiansPerToothAngle = Math.abs(Math.acos(Math.abs(xIntersect)/lastGreater));

						double radiansPerToothWidth  = 2.0*Math.asin(width/(2.0*lastGreater));; //=2.0*Math.abs( Math.atan(width/(2.0*lastGreater)) );
					//	double lastDif =  radiansPerTooth -radiansPerToothWidth*2.0;
						double theta = radiansPerTooth - radiansPerToothAngle*2.0 - radiansPerToothWidth;

						double baseWidth = 2.0*(radius)*Math.sin(theta/2f);
						double err = baseWidth - width;
						System.out.println(dif  + "\t" + err);
						newRadius =(float) lastGreater;
						
						//double radiansPerToothWidth =2.0*Math.abs( Math.atan(width/(2.0*newRadius)) );
						//ok, so the total arc per tooth should be:
					
						//double arcPerToothCurrent = radiansPerToothLocal*radius;
						

						
						//double totalArcPerToothDesired = 2.0*radiansPerToothWidth*(radius+height)+radiansPerToothAngle*2.0*radius ;
						//newRadius = (float) (totalArcPerToothDesired/radiansPerToothLocal);
						
						toothHeight.setText(String.valueOf(height));
						toothAngle.setText(String.valueOf(fromRadians((float)angle)));
						toothWidth.setText(String.valueOf(width));
						}
				}
			}catch(ActionTypeException e)
			{
				
			}
			
			
			StateManager.getToolManager().removeListener(this);
			return true;
		}
		
	};
	
	public static double quadraticFormula(double a, double b, double c) {
		//positive only, if it exists
		double det = (b*b)- 4.0*a*c;
		if(det<0.0)
			return Double.NaN;
		
		return (-b - Math.sqrt(det))/(2.0*a);
	}


	@Override
	public void close(boolean cancel) {
		StateManager.getToolManager().removeListener(matchListener);
		if(!cancel)
		{

			standardClosingBehaviour(toothAngle, GearInterpreter.TOOTH_ANGLE,Format.Angle);
			
			if(super.isAltered(toothAngle))
			{
				if(interpreter.getToothAngle()<0)
					interpreter.setToothAngle(0f);
				else if (interpreter.getToothAngle()>FastMath.HALF_PI)
					interpreter.setToothAngle(FastMath.HALF_PI);
				
			}
			
			if(newRadius>0)
			{
				interpreter.getStore().setProperty(CylinderInterpreter.CYL_RADIUS,newRadius);
				super.setValueAltered(CylinderInterpreter.CYL_RADIUS, true);
			}
			
			if(newRotation!=null)
			{
				interpreter.getStore().setProperty(CylinderInterpreter.LOCALROTATION,newRotation);
				super.setValueAltered(CylinderInterpreter.LOCALROTATION, true);
			}
			
			standardClosingBehaviour(toothHeight, GearInterpreter.TOOTH_HEIGHT);
			standardClosingBehaviour(toothWidth, GearInterpreter.TOOTH_WIDTH);
			
			
			Action<?> apply = super.apply();
			
			UndoManager.getInstance().addAction(apply);
			
	
		}
	}



	@Override
	public void open() {
		newRadius = -1f;
		newRotation = null;
		CylinderInterpreter tempInterpreter = new CylinderInterpreter(getPrototype());
		//need to load the cylinder keys also, because we might adjust them here.
		this.interpreter = new GearInterpreter(getPrototype());
		this.interpreter.loadDefaults();
		tempInterpreter.loadDefaults();
		initializePrototype();
		
		super.associateWithKey(toothAngle, GearInterpreter.TOOTH_ANGLE);
		super.associateWithKey(toothWidth, GearInterpreter.TOOTH_WIDTH);
		super.associateWithKey(toothHeight, GearInterpreter.TOOTH_HEIGHT);
		
		super.standardOpeningBehaviour(toothAngle, GearInterpreter.TOOTH_ANGLE,Format.Angle);
		super.standardOpeningBehaviour(toothWidth, GearInterpreter.TOOTH_WIDTH);
		super.standardOpeningBehaviour(toothHeight, GearInterpreter.TOOTH_HEIGHT);
	
	
	}
}
