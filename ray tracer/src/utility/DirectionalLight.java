package utility;

public class DirectionalLight {
	public Vector3D direction;
	public Color intensity;
	
	public DirectionalLight(Vector3D direction, Color intensity) {
		this.direction = direction.mul(-1);
		this.intensity = intensity;
	}
	
	public String toString() {
		return "Directional Light with intenstiy: " + intensity + " and Direction: " + direction;
	}
}
