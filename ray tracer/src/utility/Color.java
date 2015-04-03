package utility;

public class Color {
	public float r, g, b;
	public Color() {
		
		r= 1.0F;
		g= 1.0F;
		b= 1.0F;
	}
	
	public Color(float r, float g, float b) {
		this.r = r;
		this.b = b;
		this.g = g;
	
	}
	
	public Color(Color color) {
		r = color.r;
		g = color.g;
		b = color.b;		
	}
	
	public Color add(Color color) {
		float vr = Math.min(1.0f, r + color.r);
		float vg = Math.min(1.0f, g + color.g);
		float vb = Math.min(1.0f, b + color.b);	
		return new Color(vr,vg,vb);	
	}
	
	public Color divide(float scalar) {
		float nr = Math.min(1.0f, r / scalar);
		float ng = Math.min(1.0f, g / scalar);
		float nb = Math.min(1.0f, b / scalar);
		return new Color(nr,ng,nb);
	}
	
	public Color sub(Color color) {
		float vr = r - color.r;
		float vg = g - color.g;
		float vb = b - color.b;	
		return new Color(vr,vg,vb);	
	}
	public Color mul(Color color) {
		float vr = Math.min(1.0f, r * color.r);
		float vg = Math.min(1.0f, g * color.g);
		float vb = Math.min(1.0f, b * color.b);	
		return new Color(vr,vg,vb);
	}
	
	
	public Color mul(float scalar) {
		float vr = (float) Math.min(1.0f, r * scalar);
		float vg = (float) Math.min(1.0f, g * scalar);
		float vb = (float) Math.min(1.0f, b *scalar);		
		return new Color(vr,vg,vb);
	}
	
	public Color pow(int p) {
		float pr = (float) Math.pow(r, p);
		float pg = (float) Math.pow(r, g);
		float pb = (float) Math.pow(r, b);
		return new Color(pr, pg, pb);
	}
	
	public int toInteger() {
		return (int)(r*255)<<16|(int)(g*255)<<8|(int)(b*255);
	}
	public String toString() {
		return "Red: " + r + " Green: " + g + " Blue: " + b;
	}
	
	public boolean equals(Object obj) {
		Color c = (Color) obj;
		return c.r ==r & c.g == g && c.b == b;
		
	}
}
