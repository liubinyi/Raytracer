package utility;

public class Material {
   public Color kd;
   public Color ks;
   public Color ka;
   public Color kr;
   public float sp;
 
   
   public Material (Color ka,Color kd,Color ks, int sp, Color kr) {
	   this.kd = kd;
	   this.ks = ks;
	   this.ka = ka;
	   this.kr = kr;
	   this.sp = sp;
   }
   
   public String toString() {
	   return "kd: " + kd + "\n" + "ks: " + ks + "\n" + "ka: " + ka + "\n" + "kr: " + kr + "\n";
   }
}
