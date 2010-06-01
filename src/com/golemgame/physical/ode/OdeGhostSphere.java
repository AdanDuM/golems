package com.golemgame.physical.ode;

import java.util.Collection;
import java.util.Map;

import com.golemgame.model.spatial.SpatialModel;
import com.golemgame.model.spatial.shape.SphereFacade;
import com.golemgame.model.texture.TextureTypeKey.TextureShape;
import com.golemgame.mvc.PropertyStore;
import com.golemgame.mvc.golems.GhostSphereInterpreter;
import com.golemgame.physical.PhysicsComponent;
import com.golemgame.physical.ode.compile.OdePhysicsEnvironment;
import com.golemgame.states.physics.PhysicsSpatialMonitor;
import com.jme.math.Vector3f;
import com.jmex.physics.PhysicsCollisionGeometry;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.material.Material;

public class OdeGhostSphere extends OdeGhostStructure {
	private GhostSphereInterpreter interpreter;
	public OdeGhostSphere(PropertyStore store) {
		super(store);
		interpreter = new GhostSphereInterpreter(store);
	}
	
	//protected SpatialModel physicsModel = null;

	protected PhysicsCollisionGeometry collision = null;
	
/*	@Override
	public void buildCollidable(CollisionMember collidable) {
		physicsModel = new SphereModel(true);

		physicsModel.getLocalTranslation().set(interpreter.getLocalTranslation());
		physicsModel.getLocalRotation().set(interpreter.getLocalRotation());
		physicsModel.getLocalScale().set(interpreter.getExtent()).multLocal(2f);		
		
		collidable.registerCollidingModel(physicsModel);
		collidable.getModel().addChild(physicsModel);
		physicsModel.getListeners().clear();//?
		physicsModel.updateModelData();
		physicsModel.updateWorldData();
		
	}*/
	
	@Override
	protected TextureShape getPrefferedShape() {
		return TextureShape.Sphere;
	}

	public PhysicsCollisionGeometry getGhost() {
		return collision;
	}

	@Override
	public float buildCollisionGeometries(PhysicsNode physicsNode,Collection<PhysicsComponent> components,
			Vector3f store) {
		collision=physicsNode.createSphere("ghost");

		collision.getLocalTranslation().set(interpreter.getLocalTranslation());
		collision.getLocalRotation().set(interpreter.getLocalRotation());
		getParentRotation().multLocal(collision.getLocalTranslation());
		collision.getLocalRotation().set(getParentRotation().mult(collision.getLocalRotation()));
		//collision.getLocalRotation().multLocal(getParentRotation());
		collision.getLocalTranslation().addLocal(getParentTranslation());
		
		collision.getLocalScale().set(interpreter.getExtent());

		collision.setIsCollidable(false);
		
		collision.setMaterial(Material.GHOST);
		PhysicsSpatialMonitor.getInstance().registerGhost(collision);
		physicsNode.attachChild(collision);
		
		store.set(collision.getLocalTranslation());
		
		OdePhysicsComponent comp = new OdePhysicsComponent(collision,null);
		comp.setMass( 0f);
		components.add(comp);
		return 0;

	}
	
	public void buildRelationships( Map<OdePhysicalStructure, PhysicsNode> physicalMap, OdePhysicsEnvironment environment) 
	{
		//collision.getPhysicsNode().setCollisionGroup(environment.getSensorCollisionGroup());
		
		SpatialModel sensorField = new SphereFacade();
		collision.getParent().attachChild(sensorField.getSpatial());
		sensorField.getLocalTranslation().set(this.collision.getLocalTranslation());
		sensorField.getLocalScale().set(this.collision.getLocalScale().mult(2f));
		sensorField.getLocalRotation().set(this.collision.getLocalRotation());
		super.getStructuralAppearanceEffect().attachModel(sensorField);
		sensorField.updateModelData();
		
		sensorField.updateModelData();
		super.getStructuralAppearanceEffect().attachModel(sensorField);
		sensorField.setVisible(false);
		super.setSensorField(sensorField);

		
		environment.registerSensorSpatial(collision);
	}
}
