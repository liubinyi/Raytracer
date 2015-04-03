package utility;

public class Vector3D {
	public float x,y,z;
	
	public Vector3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;	
	}
	
	public Vector3D(Vector3D vector) {
		x = vector.x;
		y = vector.y;
		z = vector.z;
		
	}
	
	//add two vectors
	 public Vector3D add(Vector3D vector) {
		return new Vector3D(x+vector.x, y+vector.y, z+vector.z);
	 }
	 
	 public Vector3D mul(float t) {
			float  a = x *t;
			float	b = y*t;
			float  c = z*t;
			return new Vector3D(a,b,c);
		 }

	 //sub two vectors
	 public Vector3D sub(Vector3D vector) {
		return new Vector3D(x-vector.x, y-vector.y, z-vector.z);
	 }
	 
	 //2 vector dot product
	 public float dot(Vector3D vector) {
		 return x*vector.x + y*vector.y + z*vector.z;
	 }
	 
	 
	 //one vector one point dot product
	 public float dot(Point3D point) {
		 return x*point.x + y*point.y + z*point.z;
	 }
	 
	 public Vector3D normalize() {
			float magnitude = (float) Math.sqrt(x*x + y*y +z*z);
			float x1 = x / magnitude;
			float y1 = y / magnitude;
			float z1 = z / magnitude;
			return new Vector3D(x1,y1,z1);
			
		}
	 
	 public String toString() {
		 String str = "{" + this.x + ", " + this.y + ", " + this.z + "}";
		 return str;
	 } 
}
