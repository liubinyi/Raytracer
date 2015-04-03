import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;



public class Film {
	File image;
	BufferedImage buffer;
	int totalColor;
	int numOfColors;
	int raysPerPixel;
 
 	public Film (int width, int height, int raysPerPixel) {
 		this.image = new File("image.png");
    	this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	this.totalColor = 0;
    	this.numOfColors = 0;
    	this.raysPerPixel = raysPerPixel;
 	}
 	
 	public void commit(int x, int y, int[] colors) {
 		int color = 0;
 		for (int c : colors) {
 			color += c;
 		}
 		color /= colors.length;
 		buffer.setRGB(x,y,color);
 	}

 	public void create() {
 		try {
    		ImageIO.write(buffer, "PNG", image);
    	} catch (Exception e) {
    		System.out.println("could not write image. ");
    		System.exit(1);
    	}
 	}
 
}
