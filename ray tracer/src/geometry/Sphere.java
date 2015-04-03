package geometry;

import java.util.ArrayList;

import Jama.Matrix;
import utility.AmbientLight;
import utility.Material;
import utility.Color;
import utility.LightContainer;
import utility.ObjectContainer;
import utility.Point3D;
import utility.PointLight;
import utility.Ray;
import utility.Vector3D;
import utility.DirectionalLight;

public class Sphere extends GeometricObject {
	public Point3D center;
	public float radius;
	public Material material;
	
	public Sphere(){
		
	}
	
	public Sphere(Point3D center, float radius, Material material) {
		this.center = center;
		this.radius = radius;
		this.material = material;
	}
	
	
	 public Sphere applyTransformation(ArrayList<Object[]> transformations) {
     	Sphere sph = this;
     	for (int i = 0; i < transformations.size(); i++) {
     		Object[] curr = transformations.get(i);
     		String type = (String) curr[0];
     		Matrix t = (Matrix) curr[1];
     		if (type.equals("xfs")) {
     			sph = sph.scale(t);
     		} else if (type.equals("xft")) {
     			sph = sph.translation(t);
     		} else if (type.equals("xfr")) {
     			sph = sph.rotate(t);
     		} else {
     			System.out.println("ERROR. APPLYTRANSFORMATION HAS INCORRECT ARGUMENTS");
     		}
     	}
     	return sph;
     }
	
	public Ellipsoid scale(Matrix m) {
		double[] p1 = {radius,radius,radius, 1};
	 	Matrix P1 = new Matrix(p1,4);
	 	Matrix new_p1 = m.times(P1); 
	 	return new Ellipsoid(center,(float)new_p1.get(0,0),(float)new_p1.get(1,0),(float)new_p1.get(2,0), material);
	}
	
	 public Sphere translation(Matrix m) {
	  	double[] p1 = {center.x,center.y, center.z, 1};	
	  	Matrix P1 = new Matrix(p1,4);
	  	Matrix new_p1 = m.times(P1);       	
	  	Point3D p_new = new Point3D((float)new_p1.get(0,0),(float)new_p1.get(1,0),(float)new_p1.get(2,0));    	
	  	return new Sphere(p_new, radius, material);
  	
  }
	 
	 public Sphere rotate(Matrix m) {
		double[] p1 = {center.x, center.y, center.z, 1};
	    Matrix P1 = new Matrix(p1,4);
	    Matrix new_p1 = m.times(P1);
	  	Point3D p_new = new Point3D((float)new_p1.get(0,0),(float)new_p1.get(1,0),(float)new_p1.get(2,0));
	  	return new Sphere(p_new,radius, material);
  }
	
	
	
	

	protected float minimum_but_greaterthanone(float arg1, float arg2) {
		if (arg1 < 1 && arg2 < 1) {
			return 0.0f;
		} else if (arg1 < 1) {
			return arg2;
		} else if (arg2 < 1) {
			return arg1;
		} else if (arg1 < arg2) {
			return arg1; 
		} else {
			return arg2;
		}
		
	}
	
	
	
			
	public float hit(Ray ray) {
		float a = ray.direction.dot(ray.direction);
		float b = 2*ray.origin.sub(center).dot(ray.direction);
		float c = ray.origin.sub(center).dot(ray.origin.sub(center))-radius*radius;
		float discriminant = b*b-4*a*c;
		if (discriminant < 0.0f) {
			return 0.0f;
		} else {
			float t = (float) ((-b - Math.sqrt(discriminant))/(2*a));
			float t2 = (float) ((-b + Math.sqrt(discriminant))/(2*a));
			return  minimum_but_greaterthanone(t, t2);
		}
			
	}
	

	
	public Color computeColor(LightContainer allLights, Point3D p, Point3D eye, ObjectContainer objs, boolean ambientOn) {
		
		float maxDiffuse, maxSpecular;
		Vector3D normal = p.sub(center).normalize();
		
		Vector3D h = null;
		
		Vector3D view = eye.sub(p).normalize();

		Color diffuse = new Color(0,0,0);

		Color ambient = new Color(0,0,0);
		
		Color specular = new Color(0,0,0);

		for (DirectionalLight dlight : allLights.dlights) {
		
			
//			if the point in the shadow of another sphere, just stop.
			if (intersectP(objs, new Ray(p, dlight.direction)) != null) {
				continue;
			}
			
			dlight.direction = dlight.direction.normalize();

			maxDiffuse = max(normal.dot(dlight.direction), 0);
			
			h = dlight.direction.add(view).normalize();
		

	        maxSpecular = (float) Math.pow(max(h.dot(normal), 0), material.sp);
		
			diffuse = diffuse.add(material.kd.mul(dlight.intensity).mul(maxDiffuse));
		
			
			specular = specular.add(material.ks.mul(dlight.intensity).mul(maxSpecular));
		
		}
		
		for (PointLight plight : allLights.plights) {
		
			Vector3D direction = plight.computeDirection(p).normalize();
			
			if (intersectP(objs, new Ray(p, direction)) != null) {
				continue;
			}
		
			maxDiffuse = max(normal.dot(direction), 0);

			h = direction.add(view).normalize();

	        maxSpecular = (float) Math.pow(max(h.dot(normal), 0), material.sp);
		
			diffuse = diffuse.add(material.kd.mul(plight.intensity(p)).mul(maxDiffuse));

			specular = specular.add(material.ks.mul(plight.intensity(p)).mul(maxSpecular));
		 }
		
		for (AmbientLight alight : allLights.alights) {
			
			ambient = ambient.add(material.ka.mul(alight.intensity));
		}
		
//		}
		Color finalColor = specular.add(diffuse);
		if (ambientOn) {
			finalColor = finalColor.add(ambient);
		}
		
		return finalColor;
	}

	
	
	protected float max(float a, float b) {
		if (a > b) {
			return a;
		} return b;
	}
	
	public String toString() {
		return "Sphere at " + this.center.toString() + " with radius of " + this.radius + " and material: " + material; 
	}
	
	public boolean equals(Object object){
		Sphere s = (Sphere) object;
		return s.center.equals(center) && s.radius == radius;
	}
}
