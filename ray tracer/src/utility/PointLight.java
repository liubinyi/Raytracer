package utility;

public class PointLight {
	public Point3D position;
	public Color intensityWithoutFalloff;
	public int falloff;
	
	public PointLight(int i) {
		if (i == 0) {
			this.position = new Point3D(1,1,1);
			this.intensityWithoutFalloff = new Color(1,1,1);
			this.falloff = 0;
		} else if (i == 1) {
			this.position = new Point3D(-10,-10,-13);
			this.intensityWithoutFalloff = new Color(1,1,1);
			this.falloff = 1;
		}else if (i == 2) {
			this.position = new Point3D(10,-10,-13);
			this.intensityWithoutFalloff = new Color(1,1,1);
			this.falloff = 1;
		}else {
		this.position = new Point3D(10,10,-13);
		this.intensityWithoutFalloff = new Color(1,1,1);
		this.falloff = 1;
		}
	}
	
	public PointLight(Point3D position, Color intensity, int falloff) {
		this.position = position;
		this.intensityWithoutFalloff = intensity; 
		this.falloff = falloff;
	}
	
	public Color intensity(Point3D p) {
		Color i = intensityWithoutFalloff;
		Vector3D d = position.sub(p);
		float distance = (float) Math.sqrt(d.x*d.x + d.y*d.y +d.z*d.z);
		if (distance > 0.0f && falloff > 0) {
			i = i.divide( (float) Math.pow(distance, falloff));
		}
		return i;
	}
	public PointLight(Point3D point, Color intensityWithoutFalloff) {
		this.position = point;
		this.intensityWithoutFalloff = intensityWithoutFalloff;
	}
	
	public Vector3D computeDirection(Point3D hitpoint) {
		float x = position.x - hitpoint.x;
		float y = position.y - hitpoint.y;
		float z = position.z - hitpoint.z;
		return new Vector3D(x,y,z);
	}
	public String toString() {
		return "Point Light at " + position + " with intensity: " + intensityWithoutFalloff + " and falloff " + falloff;
	}
}

