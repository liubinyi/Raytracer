package utility;

public class AmbientLight {
	public Color intensity;
	
	public AmbientLight(Color i) {
		this.intensity = i;
	}
	public AmbientLight(float r, float g, float b){
		this.intensity = new Color(r,g,b);
	}
	
	public String toString() {
		return "Ambient Light with intenstiy: " + intensity;
	}
}
