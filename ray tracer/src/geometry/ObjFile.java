package geometry;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import utility.Point3D;

public class ObjFile {

	Scanner scanner;
	HashMap<Integer, Point3D> points;
	HashSet<Point3D[]> triangles;
	int ptNum = 0;
	int normNum = 0;
	
	
	
	public ObjFile(String file) {
		
	this.points = new HashMap<Integer, Point3D>();
	this.triangles = new HashSet<Point3D[]>();
	File source = new File(file);
	
	try {
		this.scanner = new Scanner(source);
	} catch (FileNotFoundException e) {
		
		e.printStackTrace();
	}
	
	String currentLine = null;
	while (scanner.hasNextLine()) {
		currentLine = scanner.nextLine();
		parseLine(currentLine);

	}
}
	private void parseLine(String line) {
		String[] tokens = line.split("\\s+");
		if (tokens.length < 1) {
			return;
		}
		String type = tokens[0];
		if (type.equals("v")) {
			ptNum++;
		} else if (type.equals("vn")) {
			normNum++;
		}
		switch (type) {
			case "v": 
				points.put(ptNum, new Point3D(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
				break;
			case "f":
				Point3D[] tri= {points.get(Integer.parseInt(tokens[1])), points.get(Integer.parseInt(tokens[2])), points.get(Integer.parseInt(tokens[3]))};
				triangles.add(tri);
			default:
				break;
		}
		
	}
	public HashSet<Point3D[]> getPoints() {
		return triangles;
	}
	
}
