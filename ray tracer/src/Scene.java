
import utility.Color;
import utility.LightContainer;
import utility.ObjectContainer;
import utility.Ray;
import utility.Sample;

/*main class*/
public class Scene {

	public static final int width = 1000;
	public static final int height = 1000;
    public static void main(String[] args) {
    long start = System.nanoTime();
    	
    	Parser parser = new Parser("a.txt");
    	LightContainer allLights = parser.getLights();
    	ObjectContainer objects = parser.getAllObjects();
    	Camera camera = parser.getCamera();   	
    	RayTracer raytracer = new RayTracer(objects, allLights);
    	Film film = new Film ( width,  height,  1);
    	
    	for (int i = 0; i < width; i++) {
    		
    		for (int j = 0; j < height; j++) { 
    			
    			int j2 = height - 1 - j;
    			
    			float sampleX = camera.l + (camera.r - camera.l) * (i + 0.5f) / width;
    			float sampleY = camera.b + (camera.t - camera.b) * (j2 + 0.5f) / height;
    			
    			Sample sample = new Sample(sampleX, sampleY);
    			
    			Ray ray = camera.generateRay(sample);
    			Color color = raytracer.trace(ray,allLights);
    			int[] c = {color.toInteger()};
    			film.commit(i,j,c);
    		}
    	}
    	film.create();
    	long end = System.nanoTime();//end of the time
    	
    	System.out.println("Loop Time: " + (end-start)/1000000000.0F);
    }
}
