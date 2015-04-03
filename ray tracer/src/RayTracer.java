
import geometry.Ellipsoid;
import geometry.GeometricObject;
import geometry.Sphere;
import geometry.Triangle;
import utility.Color;
import utility.LightContainer;
import utility.ObjectContainer;
import utility.Point3D;
import utility.Ray;
import utility.Vector3D;


public class RayTracer {
  ObjectContainer objects;
  LightContainer allLights;
  
  public RayTracer(ObjectContainer objects, LightContainer lights) {
	  this.objects = objects;
	  this.allLights = lights;
  }
  
  /**should return int instead of Color class or we can do return Color class*/
  public Color trace(Ray ray, LightContainer allLights) {
	  Ray ra = null;
	  float tmin = Float.MAX_VALUE;
	  boolean firstHitwasSphereorEllip = false;
	  Sphere sphereHit = null;
	  Ellipsoid ellipsoidHit = null;
	  Triangle triangleHit = null;
	  Point3D firstIntersection = null;
	  for (Sphere sphere : objects.spheres ) {	 
		 float t = sphere.hit(ray);
		 if (t < tmin && t > 0.0f) {
			 tmin = t;
			 if (sphere.getClass() == Ellipsoid.class) {
				 sphereHit = null;
				 ellipsoidHit = (Ellipsoid) sphere;
				  ra = ellipsoidHit.transformRay(ray);
						 firstIntersection = ra.ray_point(tmin);
			 } else {
				 ellipsoidHit = null;
				 sphereHit = sphere;
				 firstIntersection = ray.ray_point(tmin);
			 }
			
			 firstHitwasSphereorEllip = true;
		 }
	  }
	  
	  for (Triangle tri : objects.triangles ) {	 
			 float t = tri.hit(ray);
			 if (t < tmin && t > 0.0f) {
				 tmin = t;
				 sphereHit = null;
				 ellipsoidHit = null;
				 triangleHit = tri;
				 firstIntersection = ray.ray_point(tmin);
				 firstHitwasSphereorEllip = false;
			 }
	  }
	  
	  Color color = new Color(0,0,0);
	  
	  //nothing was hit
	  if (tmin == Float.MAX_VALUE) {
			 return color;		 
	  } else if (firstHitwasSphereorEllip) {
		  if (ellipsoidHit != null) {
			  color = new Color(ellipsoidHit.computeColor(allLights, firstIntersection, ra.origin, objects, true));
	  }
		  else {
			  color = new Color(sphereHit.computeColor(allLights, firstIntersection, ray.origin, objects, true));
			  Color r = addReflectivity(sphereHit, ray, firstIntersection, 1);
			  color = color.add(r);
		  }
	}
	  
	  else {
		  color = new Color(triangleHit.computeColor(allLights, firstIntersection, ray.origin, objects, true)); 
		 
		  Color r = addReflectivity(triangleHit, ray, firstIntersection, 1);
		  color = color.add(r);
		 }
			  return color;
		  }
	
  
  private Color addReflectivity(GeometricObject objectHit, Ray lastRay, Point3D intersection, int recDepth) {
	  
	  Color rtnColor = new Color(0,0,0);
	  Color kr = new Color(0,0,0);
	  
	 if (objectHit.getClass() == Sphere.class) {
		  Sphere s = (Sphere) objectHit;
		  kr = s.material.kr;
		  if (recDepth > 5 || s.material.kr.toInteger() == 0) {
			  return rtnColor;
		  }
	  } 
	  
	  if (objectHit.getClass() == Triangle.class) {
		  Triangle t = (Triangle) objectHit;
		  kr = t.material.kr;
		  if (recDepth > 5 || t.material.kr.toInteger() == 0) {
			  return rtnColor;
		  }
	  }
	  Ray reflectRay = createReflectRay(intersection, lastRay, objectHit);
	  Object[] o = objectHit.intersectP(objects, reflectRay);
	  if (o == null) {
		  return rtnColor;
	  }
	  
	  
	  if (o[0].getClass() == Sphere.class) {
		  Sphere sphereReflect = (Sphere) o[0];
		  float tReflect = (float) o[1];
		  Point3D reflectIntersection = reflectRay.ray_point(tReflect);
		  rtnColor = sphereReflect.computeColor(allLights, reflectIntersection, reflectRay.origin, objects, true);
		  Color reflectiveColor = addReflectivity(sphereReflect, reflectRay, reflectIntersection, recDepth + 1);
		  return rtnColor.add(reflectiveColor).mul(kr);
	  
	  } else  {
	    Triangle triangleReflect = (Triangle) o[0];	  
	    float tReflect = (float) o[1];
		  Point3D reflectIntersection = reflectRay.ray_point(tReflect);
		  rtnColor = triangleReflect.computeColor(allLights, reflectIntersection, reflectRay.origin, objects, true);
		  Color reflectiveColor = addReflectivity(triangleReflect, reflectRay, reflectIntersection, recDepth + 1);
		  return rtnColor.add(reflectiveColor).mul(kr);
	  
	  }
   } 
	  
	  
	  
	  
	
  
  
  public Ray createReflectRay(Point3D point, Ray ray, GeometricObject object) {
	  Ray reflectRay = null;
	  Vector3D n = null;
	  if (object.getClass() == Sphere.class) {
		  Sphere obj = (Sphere) object;
		  
		  n = point.sub(obj.center).normalize();
	  } if (object.getClass() == Triangle.class) {

		  Triangle obj = (Triangle) object;
		  
		  n = obj.normal;
	  }
	  Vector3D d = ray.direction.normalize();
	  
	  Vector3D direction = d.sub(n.mul(2 * d.dot(n)));
	  direction = direction.normalize();
	  reflectRay = new Ray(point.addEpilson(direction, 0.0001f), direction);
	  return reflectRay;
  }
  
}
