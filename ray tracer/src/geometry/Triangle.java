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
import Jama.*;

public class Triangle extends GeometricObject {
	
		public Point3D pointA;
		public Point3D pointB;
		public Point3D pointC;
		public Vector3D normal;
		public Material material;
		private float beta;
		private float gamma;
		private float t;
		
		public Triangle(Point3D pointA, Point3D pointB, Point3D pointC, Material m) {
			this.pointA = pointA;
			this.pointB = pointB;
			this.pointC = pointC;
			this.material = m;
			this.normal = this.find_normal(pointA, pointB, pointC);
		}
		
        public Vector3D find_normal(Point3D A, Point3D B, Point3D C) {
        	Vector3D a = B.sub(A);
        	Vector3D b = C.sub(B);
        	
        	Vector3D cross = new Vector3D((a.y*b.z - a.z*b.y), (a.z*b.x-a.x*b.z), (a.x*b.y-a.y*b.x));
        	Vector3D n = cross.normalize();
        	return n;
        }
        public Triangle applyTransformation(ArrayList<Object[]> transformations) {
        	Triangle tri = this;
        	for (int i = 0; i < transformations.size(); i++) {
        		Object[] curr = transformations.get(i);
        		String type = (String) curr[0];
        		Matrix t = (Matrix) curr[1];
        		if (type.equals("xfs")) {
        			tri = tri.scale(t);
        		} else if (type.equals("xft")) {
        			tri = tri.translation(t);
        		} else if (type.equals("xfr")) {
        			tri = tri.rotate(t);
        		} else {
        			System.out.println("ERROR. APPLYTRANSFORMATION HAS INCORRECT ARGUMENTS");
        		}
        	}
        	return tri;
        }
        
        public Triangle scale(Matrix m) {
        	
        	
        	double[] p1 = {pointA.x, pointA.y, pointA.z, 1};
        	double[] p2 = {pointB.x, pointB.y, pointB.z, 1};
        	double[] p3 = {pointC.x, pointC.y, pointC.z,1};
        	
        	Matrix P1 = new Matrix(p1,4);
        	Matrix P2 = new Matrix(p2,4);
        	Matrix P3 = new Matrix(p3,4);
        	
        	
        	Matrix new_p1 = m.times(P1);       	
        	Matrix new_p2 = m.times(P2);
        	Matrix new_p3 = m.times(P3);
        	
        	Point3D p_new = new Point3D((float)new_p1.get(0,0),(float)new_p1.get(1,0),(float)new_p1.get(2,0));
        	Point3D p2_new = new Point3D((float)new_p2.get(0,0),(float)new_p2.get(1,0),(float)new_p2.get(2,0));
        	Point3D p3_new = new Point3D((float)new_p3.get(0,0),(float)new_p3.get(1,0),(float)new_p3.get(2,0));
        	return new Triangle(p_new,p2_new,p3_new, material);
        }
        
        public Triangle rotate(Matrix m) {
        	
        	double[] p1 = {pointA.x, pointA.y, pointA.z, 1};
        	double[] p2 = {pointB.x, pointB.y, pointB.z, 1};
        	double[] p3 = {pointC.x, pointC.y, pointC.z, 1};
        	Matrix P1 = new Matrix(p1,4);
        	Matrix P2 = new Matrix(p2,4);
        	Matrix P3 = new Matrix(p3,4);
        	
        	Matrix new_p1 = m.times(P1);       	
        	Matrix new_p2 = m.times(P2);
        	Matrix new_p3 = m.times(P3);
        	
        	Point3D p_new = new Point3D((float)new_p1.get(0,0),(float)new_p1.get(1,0),(float)new_p1.get(2,0));
        	Point3D p2_new = new Point3D((float)new_p2.get(0,0),(float)new_p2.get(1,0),(float)new_p2.get(2,0));
        	Point3D p3_new = new Point3D((float)new_p3.get(0,0),(float)new_p3.get(1,0),(float)new_p3.get(2,0));
        	return new Triangle(p_new,p2_new,p3_new, material);
        }
        
        public Triangle translation(Matrix m) {

        	
        	double[] p1 = {pointA.x, pointA.y, pointA.z, 1};
        	double[] p2 = {pointB.x, pointB.y, pointB.z, 1};
        	double[] p3 = {pointC.x, pointC.y, pointC.z, 1};
        	
        	Matrix P1 = new Matrix(p1,4);
        	Matrix P2 = new Matrix(p2,4);
        	Matrix P3 = new Matrix(p3,4);
        	
        	Matrix new_p1 = m.times(P1);       	
        	Matrix new_p2 = m.times(P2);
        	Matrix new_p3 = m.times(P3);
        	
        	Point3D p_new = new Point3D((float)new_p1.get(0,0),(float)new_p1.get(1,0),(float)new_p1.get(2,0));
        	Point3D p2_new = new Point3D((float)new_p2.get(0,0),(float)new_p2.get(1,0),(float)new_p2.get(2,0));
        	Point3D p3_new = new Point3D((float)new_p3.get(0,0),(float)new_p3.get(1,0),(float)new_p3.get(2,0));
        	return new Triangle(p_new,p2_new,p3_new, material);
        	
        }
		public float hit(Ray ray) {	
			
				
				double[] x = {pointA.x-pointB.x, pointA.x-pointC.x, ray.direction.x};
				double[] y = {pointA.y-pointB.y, pointA.y-pointC.y, ray.direction.y};
				double[] z = {pointA.z-pointB.z, pointA.z-pointC.z, ray.direction.z};
				
				double[] w = {pointA.x-ray.origin.x, pointA.y-ray.origin.y,  pointA.z-ray.origin.z};
				double[][] array = {x, y, z};
								
				Matrix A = new Matrix(array);			
				Matrix b = new Matrix(w,3);
				Matrix j = null;
				try{
					j = A.solve(b);
				}
				catch (RuntimeException e) {
					return 0.0f;
				}
				
				beta = (float) j.get(0,0);
				gamma =  (float) j.get(1,0);
				t = (float) j.get(2,0);
				
				
				if (t < 1.0f) {
					return 0.0f;
				} else if (gamma < 0 || gamma > 1) {
					return 0.0f;
				} else if (beta < 0  || (beta > 1-gamma)) {
					return 0.0f;
				} else {
					return t;
				}
				
		}
		

		
		public Color computeColor(LightContainer allLights, Point3D p, Point3D eye, ObjectContainer objs, boolean ambientOn) {

			float maxDiffuse, maxSpecular;
			
			Vector3D h = null;		
			Vector3D view = eye.sub(p).normalize();

			Color diffuse = new Color(0,0,0);
			Color ambient = new Color(0,0,0);
			Color specular = new Color(0,0,0);

			for (DirectionalLight dlight : allLights.dlights) {
							
				//if the point in the shadow of another sphere, just stop.
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
			Color finalColor = diffuse.add(specular);
			
			if (ambientOn) {
				finalColor = finalColor.add(ambient);
			}
			return finalColor;
		}
      
		private float max(float a, float b) {
			if (a > b) {
				return a;
			} return b;
		}
		
		
		public boolean equals(Object object){
			Triangle t = (Triangle) object;
			return  pointA.equals(t.pointA) && pointB.equals(t.pointB) && pointC.equals(t.pointC);
		}
		
		public String toString() {
			return "Triangle with vertices at  " + pointA + " " + pointB + " " + pointC + " and material is: " + material;
		}
}