package com.golemgame.structural.structures;

import java.util.Collection;

import com.golemgame.mechanical.StructuralMachine;
import com.golemgame.model.Model;
import com.golemgame.model.ParentModel;
import com.golemgame.model.spatial.NodeModel;
import com.golemgame.model.spatial.SpatialModel;
import com.golemgame.model.spatial.shape.CameraFacade;
import com.golemgame.model.spatial.shape.CameraModel;
import com.golemgame.mvc.PropertyStore;
import com.golemgame.mvc.golems.CameraInterpreter;
import com.golemgame.properties.Property;
import com.golemgame.states.camera.EmbeddedCamera;
import com.golemgame.structural.collision.CollisionMember;
import com.golemgame.structural.collision.NonPropagatingCollisionMember;
import com.jme.renderer.ColorRGBA;

public class CameraStructure extends FunctionalStructure  {
	private static final long serialVersionUID = 1L;

	private SpatialModel cameraModel;
	private SpatialModel cameraFacade;
	private NodeModel model;
	private CollisionMember collisionMember;
	private Model[] controlledModels;
	
	private EmbeddedCamera embeddedCamera;

	
	private CameraInterpreter interpreter;
	public CameraStructure(PropertyStore store) {
		super(store);
		this.interpreter = new CameraInterpreter(store);
		this.model = new NodeModel(this);
	
		
	
		
		cameraModel=new CameraModel(true);
	
		cameraFacade = new CameraFacade();
		
		this.getModel().addChild(cameraModel);
		this.getModel().addChild(cameraFacade);
		
		embeddedCamera = new EmbeddedCamera();
		//embeddedCamera.getCameraModel().getLocalRotation().multLocal(new Quaternion().fromAngleNormalAxis(0,Vector3f.UNIT_Y));
		this.model.addChild(embeddedCamera.getCameraModel());
		
	
		
	
			this.collisionMember = new NonPropagatingCollisionMember(model,this.getActionable());
			collisionMember.registerCollidingModel(cameraModel);
		
		
		cameraModel.setActionable(this);
		controlledModels = new Model[]{cameraModel,cameraFacade};
		super.initialize();
		
		//this.getAppearance().addEffect(new TintableColorEffect(new ColorRGBA(0.1f,0.1f,0.1f,1f)),true);
		this.getStructuralAppearanceEffect().setBaseColor(new ColorRGBA(0.2f,0.2f,0.2f,0.8f));
	}

	
	@Override
	public void addToMachine(StructuralMachine machine) {
		super.addToMachine(machine);
		machine.getEnvironment().addCameraDelegate(embeddedCamera,interpreter.getReference());
		
	}


	protected Model[] getControlledModels() {

		return controlledModels;
	}

	
	protected CollisionMember getStructuralCollisionMember() {

		return collisionMember;
	}

	@Override
	public Collection<Property> getPropertySet() {
		 Collection<Property> properties = super.getPropertySet();
		 properties.add( new Property(Property.PropertyType.CAMERA,this.interpreter.getStore()));
		 
		return properties;
	}

	public ParentModel getModel() {

		return model;
	}

	
	public boolean isPropagating() {
		return false;
	}


	

	public boolean isPhysical() {
		return true;
	}

	


	
	
	@Override
	public void remove() {
		getMachine().getEnvironment().removeCameraDelegate(embeddedCamera);
		
		super.remove();
	}


	@Override
	public void refresh() {
		super.refresh();
		embeddedCamera.setLockAll(interpreter.isOrientationLocked());
		this.embeddedCamera.lockRollPitchYaw(interpreter.isRollLocked(), interpreter.isPitchLocked(), interpreter.isYawLocked());
	}

/*
	@Override
	public void refreshView() {
		Layer layer = getLayer();
		if (getViews().contains(Viewable.ViewMode.FUNCTIONAL))
		{
			if(layer.isEditable())
			{
				super.setSelectable(true);
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
	}*/
	
}
