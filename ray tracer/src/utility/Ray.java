package utility;

public class Ray {
	
	public Point3D origin;
	public Vector3D direction;
	
	public Ray(Point3D origin, Vector3D direction) {
		
		this.origin = new Point3D(origin);
		this.direction = new Vector3D(direction);
	}
	
	public Point3D ray_point (float t) {
		
		return origin.add(direction.mul(t));
	}
	public String toString() {
		 String str = "Point: " + origin.toString() + "Direction: " + direction.toString();
		 return str;
	 } 
	
}
