package utility;

public class Point3D {
 public float x,y,z;
 
 //constructor
 public Point3D (float x, float y, float z) {
	 this.x = x;
	 this.y = y;
	 this.z = z;	
 }
 
 //copy constructor
 public Point3D(Point3D point) {
	 x = point.x;
	 y = point.y;
	 z = point.z;
 }

 
 public Point3D addEpilson(Vector3D direction, float t) {
	 return new Point3D(this.add(direction.mul(t)));
 }
 
//add two point
 public Point3D add(Point3D point) {
	return new Point3D(x+point.x, y+point.y, z+point.z);
 }

 //sub two point
 public Vector3D sub(Point3D point) {
	return new Vector3D(x-point.x, y-point.y, z-point.z);
 }
 
 public Point3D add(Vector3D v) {
	return new Point3D(x+v.x, y+v.y, z+v.z);
 }

//two point dot product
 public float dot(Point3D point) {
	 return x*point.x + y*point.y + z*point.z;
 }
 
//one vector one point dot product
 public float dot(Vector3D vector) {
	 return (float) (x*vector.x + y*vector.y + z*vector.z);
 }
 
 public String toString() {
	 String str = "{" + this.x + ", " + this.y + ", " + this.z + "}";
	 return str;
 } 
 
 public boolean equals(Object object){
	 Point3D p = (Point3D) object;
	 return p.x == x && p.y == y && p.z == z;
 }
}
