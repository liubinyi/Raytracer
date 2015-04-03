package utility;


public class LightContainer {

	public DirectionalLight[] dlights;
	public PointLight[] plights;
	public AmbientLight[] alights;
	

	public LightContainer(DirectionalLight[] dlights, PointLight[] plights, AmbientLight[] alights) {
		this.dlights = dlights;
		this.plights = plights;
		this.alights = alights; 
	}
	
	public String toString() {
		String rtn = "Ambient: \n";
		for (AmbientLight a : alights) {
			rtn = rtn + a + "\n";
		}
		rtn += "Point: \n";
		for (PointLight p : plights) {
			rtn += p + "\n";
		}
		for (DirectionalLight d : dlights) {
			rtn += d + "\n";
		}
		return rtn;
	}
}
