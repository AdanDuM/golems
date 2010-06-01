package com.golemgame.model.spatial.collision;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Capsule;

public class CapsulePointCollisionTest extends PointCollisionTest {


	
	public boolean containsPoint(Vector3f point,Spatial toTest) {
		//assume that the spatial is a box
		Capsule cap = (Capsule) toTest;
		
		//tranform the point into the box's coordinate system
		
        Vector3f testPoint = PointCollisionTest.transformPoint(point, toTest);
		
        return capsuleContainsPoint(testPoint, cap.getRadius(),cap.getHeight());
	}
	
	/**
	 * Note: height is the height of the cylinder part of this capsule (not including the domes capping each end).
	 * @param testPoint
	 * @param radius
	 * @param height
	 * @return
	 */
	public static boolean capsuleContainsPoint(Vector3f testPoint, float radius, float height)
	{
        if (cylinderContainsPoint(testPoint, radius, height))
        	return true;
        	
        //test the top and bottom spheres
        
        //translate the test point into the top (half-)sphere's collision system
        testPoint.y -=height/2f;
        if( SpherePointCollisionTest.sphereContainsPoint(testPoint, radius))
        	return true;
        
        //translate the test point into the bottom (half-)sphere's collision system
        testPoint.y += height;
        return SpherePointCollisionTest.sphereContainsPoint(testPoint, radius);

	}
	
	/**
	 * The point must be transformed into local coordinates first (using PointCollisionTest.transformPoints)
	 * @param point
	 * @param radius
	 * @param height
	 * @return
	 */
	private static boolean cylinderContainsPoint(Vector3f testPoint, float radius, float height)
	{

		if (FastMath.abs(testPoint.y) > height/2f)
			return false;
			
		if ((testPoint.z * testPoint.z) + (testPoint.x * testPoint.x)> radius*radius)
			return false;
		
		
		
		return true;
	}
	
	


}
