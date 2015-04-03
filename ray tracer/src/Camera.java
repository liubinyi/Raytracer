import utility.Point3D;
import utility.Ray;
import utility.Sample;
import utility.Vector3D;

public class Camera {
	Point3D eye;
	Point3D UL;
	Point3D UR;
	Point3D LR;
	Point3D LL;
	float l;
	float r;
	float b;
	float t;
	

    
    public Camera(Point3D eye, Point3D LL, Point3D LR, Point3D UL, Point3D UR){
    	this.eye = eye;
    	this.UL = UL;
    	this.UR = UR;
    	this.LR = LR;
    	this.LL = LL;
    	this.l = this.LL.x;
    	this.r = this.UR.x;
    	this.b = this.LL.y;
    	this.t = this.UR.y;
    }
    
    public Camera(Point3D[] arr) {
    	this.eye = arr[0];
    	this.LL = arr[1];
    	this.LR = arr[2];
    	this.UL = arr[3];
    	this.UR = arr[4];
    	this.l = this.LL.x;
    	this.r = this.UR.x;
    	this.b = this.LL.y;
    	this.t = this.UR.y;
    }

    public Ray generateRay(Sample sample) {
    	float dist =  Math.abs(LR.z - eye.z);
    	Vector3D direction = new Vector3D(sample.x,sample.y,-dist) ;
    	Ray ray = new Ray(eye, direction);
    	return ray;
    	
    }
    
    public String toString() {
    	return "Eye: " + eye + " UL: " + UL + " UR: " + UR + " LL: " + LL + " LR: " + LR;
    }
}
