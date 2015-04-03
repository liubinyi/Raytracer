package geometry;

import java.util.ArrayList;

import utility.AmbientLight;
import utility.Color;
import utility.DirectionalLight;
import utility.LightContainer;
import utility.Material;
import utility.ObjectContainer;
import utility.Point3D;
import utility.PointLight;
import utility.Ray;
import utility.Vector3D;
import Jama.Matrix;

public class Ellipsoid extends Sphere {
	public Point3D center;
	public float r1;
	public float r2;
	public float r3;
	public Material material;
	
    public Ellipsoid(Point3D center, float r1, float r2, float r3, Material material) {
    	this.center = center;
    	this.r1 = r1;
    	this.r2 = r2;
    	this.r3 = r3;
    	this.material = material;
    	
    }
    
    public Ellipsoid applyTransformation(ArrayList<Object[]> transformations) {
     	Ellipsoid sph = this;
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
    
    public Ellipsoid translation(Matrix m) {    	
     	double[] p1 = {center.x,center.y, center.z, 1};
     	
     	
     	Matrix P1 = new Matrix(p1,4);
     	Matrix new_p1 = m.times(P1);       	
     	
     	
     	Point3D p_new = new Point3D((float)new_p1.get(0,0),(float)new_p1.get(1,0),(float)new_p1.get(2,0));    	
     	return new Ellipsoid(p_new, r1, r2,r3, material);
     	
     }
    public Ellipsoid scale(Matrix m) {
    	double[] p1 = {r1,r2,r3, 1};
    	Matrix P1 = new Matrix(p1,4);
    	Matrix new_p1 = m.times(P1); 
    	return new Ellipsoid(center,(float)new_p1.get(0,0),(float)new_p1.get(1,0),(float)new_p1.get(2,0), material);
    }
    
    public Ellipsoid rotate(Matrix m) {
    	double[] p1 = {center.x, center.y, center.z, 1};

     	Matrix P1 = new Matrix(p1,4);
     	
     	
     	Matrix new_p1 = m.times(P1);
     	
     	
     	
     	Point3D p_new = new Point3D((float)new_p1.get(0,0),(float)new_p1.get(1,0),(float)new_p1.get(2,0));
     	return new Ellipsoid(p_new,r1,r2,r3, material);
     }
    public Sphere transform_to_sphere () {
    	double[] a = {r1, 0, 0, 0};
    	double[] b = {0, r2, 0, 0};
    	double[] c = {0, 0, r3, 0};
    	double[] d = {0, 0, 0, 1};
    	double[] r = {r1, r2, r3, 0};
    	double[][] array = {a, b, c, d};
    	Matrix A = new Matrix(array);
    	Matrix back = A.inverse();
    	
    	Matrix C = new Matrix(r, 4);
    	Matrix sphere_r = back.times(C);
    	return new Sphere(new Point3D(center.x, center.y, center.z), (float)sphere_r.get(0,0), material);
    }
    
   
    public float hit(Ray ray) {
    	Ray newRay = transformRay(ray);
    	Sphere s = transform_to_sphere();
		float a = newRay.direction.dot(newRay.direction);
		float b = 2*newRay.origin.sub(s.center).dot(newRay.direction);
		float c = newRay.origin.sub(s.center).dot(newRay.origin.sub(s.center))-s.radius*s.radius;
		float discriminant = b*b-4*a*c;
		if (discriminant < 0.0f) {
			return 0.0f;
		} else {
			float t = (float) ((-b - Math.sqrt(discriminant))/(2*a));
			float t2 = (float) ((-b + Math.sqrt(discriminant))/(2*a));
			return  minimum_but_greaterthanone(t, t2);
		}
			
	}
    public Matrix getTransformM() {
    	double[] a = {r1, 0, 0, center.x};
    	double[] b = {0, r2, 0, center.y};
    	double[] c = {0, 0, r3, center.z};
    	double[] d = {0, 0, 0, 1};
    	double[][] array = {a, b, c, d};
    	Matrix A = new Matrix(array);
    	Matrix back = A.inverse();
    	return back;
    }
    public Ray transformRay(Ray ray) {
    	Matrix m = getTransformM();
    	double[] d = {ray.direction.x,ray.direction.y, ray.direction.z, 0};
    	double[] o = {ray.origin.x, ray.origin.y, ray.origin.z, 1};
    	Matrix D = new Matrix(d, 4);
    	Matrix O = new Matrix(o, 4);
    	
    	Matrix X = m.times(D); 
    	Matrix Y = m.times(O);
    	
    	Vector3D direction = new Vector3D((float) X.get(0,0),(float) X.get(1,0),(float)X.get(2,0));
    	Point3D origin = new Point3D((float) Y.get(0,0),(float) Y.get(1,0),(float) Y.get(2,0));
    	return new Ray(origin, direction);
    }
    
    public Vector3D transformN(Vector3D n) {
    	Matrix m = getTransformM();
    	Matrix m2 = m.transpose();
    	double[] d = {n.x, n.y, n.z, 0};
    	Matrix N = new Matrix(d, 4);
    	Matrix transN = m2.times(N);
    	Vector3D newNormal = new Vector3D((float) transN.get(0,0),(float) transN.get(1,0),(float) transN.get(2,0));
    	return newNormal;
    }
    
    public Color computeColor(LightContainer allLights, Point3D p, Point3D eye, ObjectContainer objs, boolean ambientOn) {

		float maxDiffuse, maxSpecular;
		
		Vector3D before = p.sub(center);
		Vector3D normal = transformN(before);
				
		Vector3D h = null;
		
		Vector3D view = eye.sub(p);
		Ray r = new Ray(p, view);
		
		r = transformRay(r);
		view = r.direction;

		Color diffuse = new Color(0,0,0);

		Color ambient = new Color(0,0,0);
		
		Color specular = new Color(0,0,0);

		for (DirectionalLight dlight : allLights.dlights) {
		
			
			dlight.direction = dlight.direction.normalize();

			maxDiffuse = max(normal.dot(dlight.direction), 0);
			
			h = dlight.direction.add(view).normalize();
		

	        maxSpecular = (float) Math.pow(max(h.dot(normal), 0), material.sp);
	   
			
		
			diffuse = diffuse.add(material.kd.mul(dlight.intensity).mul(maxDiffuse));
		
			
			specular = specular.add(material.ks.mul(dlight.intensity).mul(maxSpecular));
		
		}
		
		for (PointLight plight : allLights.plights) {
		

			
			Vector3D direction = plight.computeDirection(p);
			Ray lightRay = new Ray(plight.position, direction);
			
			lightRay = transformRay(lightRay);
			direction = lightRay.direction;
		
			maxDiffuse = max(normal.dot(direction), 0);

			h = direction.add(view).normalize();

	        maxSpecular = (float) Math.pow(max(h.dot(normal), 0), material.sp);
			diffuse = diffuse.add(material.kd.mul(plight.intensity(p)).mul(maxDiffuse));

			specular = specular.add(material.ks.mul(plight.intensity(p)).mul(maxSpecular));
		 }
		
		for (AmbientLight alight : allLights.alights) {
			
			ambient = ambient.add(material.ka.mul(alight.intensity));
		}
		
		
		Color finalColor = ambient.add(diffuse).add(specular);
		
		return finalColor;
	}
    
    
	public String toString() {
		return "Ellipsoid at " + this.center.toString() + " with x, y, z radii of " + this.r1 + " " + this.r2 + " " + this.r3 + " and material: " + material; 
	}
}