package utility;

import geometry.Sphere;
import geometry.Triangle;

/**Sphere and Triangle will be arraylist*/
public class ObjectContainer {
    public Sphere[] spheres;
    public Triangle[] triangles;
	public ObjectContainer(Sphere[] spheres, Triangle[] triangles) {
		this.spheres = spheres;
		this.triangles = triangles;
		
	}
	
	public String toString() {
		String rtn = "Spheres: \n";
		for (Sphere s : spheres) {
			rtn = rtn + s + "\n";
		}
		rtn += "Triangles: \n";
		for (Triangle t : triangles) {
			rtn += t + "\n";
		}rtn += "Elliposids: \n";
		return rtn;
	}
}