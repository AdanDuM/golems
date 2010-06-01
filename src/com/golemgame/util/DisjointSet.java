package com.golemgame.util;

/**
 * An efficient Disjoint Set Data Structure.
 * This set is not object oriented; elements are simply integers.
 * It should be wrapped by an object oriented structure that maps objects to elements.
 * @author Sam
 *
 */
public class DisjointSet {
	private int[] parent;

	public DisjointSet(int numElements) {
		super();
		parent = new int[numElements];
		
		reset();
	}
	
	/**
	 * Get the set that x belongs to.
	 * @param x
	 * @return
	 */
	public int find(int x)
	{
		if(parent[x] < 0)
			return x;
		else{
			parent[x] = find(parent[x]);
			return parent[x];			
		}
	}
	
	/**
	 * Union the two trees that x and y belong to.
	 * @param x
	 * @param y
	 * @return
	 */
	public void union(int x, int y)
	{
		if(x == y)
			return;
		
		if(parent[x]>= 0) //x is not a root
			x = find(x);
		if(parent[y]>=0)
			y = find(y);
		
		//now they are both roots
		if(x == y)
			return; //check again that they belong to different trees
		
		if(parent[x] < parent[y])
		{
			parent[y] = x;
		}else{
			if(parent[x] == parent[y])
			{
				parent[y] --;
			}
			parent[x] = y;
		}
		
	}
	
	/**
	 * Puts all elements in their own set.
	 */
	public void reset()
	{
		for(int i = 0;i<parent.length;i++)
			parent[i] = -1;
	}
	
	public int size()
	{
		return parent.length;
	}
	
	/**
	 * Resize the sets, maintaining the current assignmnets
	 * @param newSize
	 */
	public void resize(int newSize)
	{
		int[] newParent = new int[newSize];
		System.arraycopy(parent,0, 0, 0, Math.min(newParent.length,parent.length));
		parent = newParent;
	}
}
