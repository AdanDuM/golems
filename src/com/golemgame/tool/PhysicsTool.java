package com.golemgame.tool;

import java.util.Collection;

import com.golemgame.model.Model;
import com.golemgame.physical.SpringController;
import com.golemgame.physical.PhysicsComponent;
import com.golemgame.physical.PhysicsInteractionManager;
import com.golemgame.states.StateManager;
import com.golemgame.tool.action.Actionable;
import com.jme.math.Vector2f;

public class PhysicsTool implements IActionTool {

	private final PhysicsInteractionManager interactionManager;
	private final SpringController spring;


	public PhysicsTool(PhysicsInteractionManager interactionManager,
			SpringController spring) {
		super();
		this.interactionManager = interactionManager;
		this.spring = spring;
	}

	
	public boolean mouseButton(int button, boolean pressed, int x, int y) {
		if(button == 0 && pressed)
		{
			
			PhysicsComponent comp = interactionManager.pickDynamicPhysics(new Vector2f(x,y));
			if(comp!=null)
			{
				spring.setTarget(comp.getParent());
			}else
			{
				spring.setTarget(null);
				return  StateManager.getToolPool().getCameraTool().mouseButton(button, pressed, x, y);
			}
		}else if(button ==0 &! pressed)
		{
			spring.setTarget(null);
			return StateManager.getToolPool().getCameraTool().mouseButton(button, pressed, x, y);
		}
		return true;
	}


	public void mouseMovementAction(Vector2f mousePos, boolean left,
			boolean right) {
		if(spring!=null)
			spring.setPosition(mousePos.x,mousePos.y);
		StateManager.getToolPool().getCameraTool().mouseMovementAction(mousePos, left, right);
	}

	public void scrollMove(int wheelDelta, int x, int y) {
		if(spring!=null)
			spring.setPosition(x,y);
		StateManager.getToolPool().getCameraTool().scrollMove(wheelDelta, x, y);
	}

	public void deselect(Actionable actionable) {
		// TODO Auto-generated method stub
		
	}

	public Actionable getPrimarySelection() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Actionable> getSelectedActionables() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSelected() {
		// TODO Auto-generated method stub
		return false;
	}

	public void select(Actionable toSelect, Model selectedModel,
			boolean primarySelection) throws FailedToSelectException {
		// TODO Auto-generated method stub
		
	}

	public void copy() {
		// TODO Auto-generated method stub
		
	}

	public void delete() {
		// TODO Auto-generated method stub
		
	}

	public void deselect() {
		// TODO Auto-generated method stub
		
	}

	public void focus() {
		// TODO Auto-generated method stub
		
	}

	public void properties() {
		// TODO Auto-generated method stub
		
	}

	public void showPrimaryEffect(boolean show) {
		// TODO Auto-generated method stub
		
	}

	public void xyPlane(boolean value) {
		// TODO Auto-generated method stub
		
	}

	public void xzPlane(boolean value) {
		// TODO Auto-generated method stub
		
	}

	public void yzPlane(boolean value) {
		// TODO Auto-generated method stub
		
	}

}
