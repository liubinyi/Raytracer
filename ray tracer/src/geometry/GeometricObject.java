package geometry;

import Jama.Matrix;
import utility.Material;
import utility.Color;
import utility.ObjectContainer;
import utility.Ray;

public abstract class GeometricObject {
	
	public Color color;
	public Material material;
	
	public abstract float hit(Ray ray);

	private float findTriangleT(float beta, float gamma, float t) {
		if (t < 0.0f) {
			return 0.0f;
		} else if (gamma < 0 || gamma > 1) {
			return 0.0f;
		} else if (beta < 0  || (beta > 1-gamma)) {
			return 0.0f;
		} else {
			return t;
		}
	}
	
	private float minimum_but_greaterthanzero(float arg1, float arg2) {
		if (arg1 < 0.0f && arg2 < 0.0f) {
			return 0.0f;
		} else if (arg1 < 0.0f) {
			return arg2;
		} else if (arg2 < 0.0f) {
			return arg1;
		} else if (arg1 < arg2) {
			return arg1; 
		} else {
			return arg2;
		}
		
	}
	
	public Object[] intersectP(ObjectContainer objs, Ray ray) {
		float a = ray.direction.dot(ray.direction);
		float tminSphere = Float.MAX_VALUE;
		float tminTri = Float.MAX_VALUE;
		Sphere sphereHit = null;
		Triangle triangleHit = null;
		for (int i = 0; i < objs.spheres.length; i++) {
			Sphere s = objs.spheres[i];
			
			if (this.getClass() == Sphere.class && s.equals(this)) {
				continue;
			}
			
			
			float b = 2*ray.origin.sub(s.center).dot(ray.direction);
			float c = ray.origin.sub(s.center).dot(ray.origin.sub(s.center))-s.radius*s.radius;
			float discriminant = b*b-4*a*c;
			if (discriminant < 0.0f) {
				continue;
			} else {
				
				float t = (float) ((-b - Math.sqrt(discriminant))/(2*a));
				float t2 = (float) ((-b + Math.sqrt(discriminant))/(2*a));
				
				float currentT = minimum_but_greaterthanzero(t, t2);
				
				if (currentT < tminSphere && currentT > 0.0f) {
					sphereHit = s;
					tminSphere = currentT;
				}
			}
		
		}
		
		for (int j = 0; j < objs.triangles.length; j++) {
			Triangle tri = objs.triangles[j];
			
			if (this.getClass() == Triangle.class && tri.equals(this)) {
				continue;
			}
			
			double[] x = {tri.pointA.x-tri.pointB.x, tri.pointA.x-tri.pointC.x, ray.direction.x};
			double[] y = {tri.pointA.y-tri.pointB.y, tri.pointA.y-tri.pointC.y, ray.direction.y};
			double[] z = {tri.pointA.z-tri.pointB.z, tri.pointA.z-tri.pointC.z, ray.direction.z};
			
			double[] w = {tri.pointA.x-ray.origin.x, tri.pointA.y-ray.origin.y,  tri.pointA.z-ray.origin.z};
			double[][] array = {x, y, z};
							
			Matrix A = new Matrix(array);			
			Matrix b = new Matrix(w,3);
			Matrix m = null;
			
			try{
				m = A.solve(b);
				
			} catch (RuntimeException e) {
				return null;
			}
			
			
			float beta = (float) m.get(0,0);
			float gamma =  (float) m.get(1,0);
			float t = (float) m.get(2,0);
			
			float currentT = findTriangleT(beta, gamma, t);
			if (currentT < tminTri && currentT > 0.0f) {
				triangleHit = tri;
				tminTri = currentT;
			}
//			
		}
		
		if (tminSphere == Float.MAX_VALUE && tminTri == Float.MAX_VALUE) {
			return null;
		}
		Object[] rtn = new Object[2];
		
		if (tminSphere < tminTri) {
			rtn[0] = sphereHit;
			rtn[1] = tminSphere;	
		} else {
			rtn[0] = triangleHit;
			rtn[1] = tminTri;
		}
		return rtn;
			
	}
	
	
}
