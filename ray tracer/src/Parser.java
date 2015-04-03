
import geometry.ObjFile;
import geometry.Sphere;
import geometry.Triangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import utility.AmbientLight;
import utility.Color;
import utility.DirectionalLight;
import utility.LightContainer;
import utility.Material;
import utility.ObjectContainer;
import utility.Point3D;
import utility.PointLight;
import utility.Transformation;
import utility.Vector3D;

import Jama.Matrix;


public class Parser {

	Scanner scanner;
	Camera cam;
	Material currentMaterial;
	HashSet<Sphere> spheres;
	HashSet<Triangle> triangles;
	HashSet<AmbientLight> alights;
	HashSet<PointLight> plights;
	HashSet<DirectionalLight> dlights;
	ArrayList<Object[]> transformationAndMatrix; 
	
	


	public Parser(String file) {
		this.scanner = null;
		this.cam = null;
		this.transformationAndMatrix = new ArrayList<Object[]>();
		this.currentMaterial = null;
		this.spheres = new HashSet<Sphere>();
		this.triangles = new HashSet<Triangle>();
		this.alights = new HashSet<AmbientLight>();
		this.plights = new HashSet<PointLight>();
		this.dlights = new HashSet<DirectionalLight>();

		
		
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
		if (line.equals("\n")) {
			return;
		}
		String[] tokens = line.split("\\s+");
		if (tokens.length <= 1 && !tokens[0].equals("xfz")) {
			return;
		}
		String type = tokens[0];
		switch (type) {
			case "cam":
				if (tokens.length != 16) {
					System.out.println("ERROR. Line should have 16 tokens. Currently has " + tokens.length);
				}
				Point3D[] points = new Point3D[5];
				for (int i = 1; i < tokens.length; i = i + 3) {
					points[(i-1)/3] = new Point3D(Float.parseFloat(tokens[i]), Float.parseFloat(tokens[i+1]), Float.parseFloat(tokens[i+2]));
				}
				this.cam = new Camera(points);	
				break;
				
			case "sph":
				if (tokens.length != 5) {
					System.out.println("ERROR. Line should have 5 tokens. Currently has " + tokens.length);
				}
				Point3D center = new Point3D(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				Sphere sph = new Sphere(center, Float.parseFloat(tokens[4]), currentMaterial);
				sph = sph.applyTransformation(transformationAndMatrix);
					this.spheres.add(sph);
				break;
				
			case "tri":
				if (tokens.length != 10) {
					System.out.println("ERROR. Line should have 10 tokens. Currently has " + tokens.length);
				}
				Point3D a = new Point3D(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				Point3D b = new Point3D(Float.parseFloat(tokens[4]), Float.parseFloat(tokens[5]), Float.parseFloat(tokens[6]));
				Point3D c = new Point3D(Float.parseFloat(tokens[7]), Float.parseFloat(tokens[8]), Float.parseFloat(tokens[9]));
				Triangle tri = new Triangle(a, b, c, currentMaterial);
				tri = tri.applyTransformation(transformationAndMatrix);
				this.triangles.add(tri);
				
				
				break;
				
			case "obj":
				if (tokens.length != 2) {
					System.out.println("ERROR. Line should have 2 tokens. Currently has " + tokens.length);
				}
				ObjFile obj = new ObjFile(tokens[1]);
				for (Point3D[] v : obj.getPoints()) {
						Triangle currT = new Triangle(v[0], v[1], v[2], currentMaterial);
						currT = currT.applyTransformation(transformationAndMatrix);
						triangles.add(currT);
				}
				
				break;
				
			case "ltp":
				if (tokens.length != 7 && tokens.length != 8) {
					System.out.println("ERROR. Line should have 7 or 8 tokens. Currently has " + tokens.length);
				}
				Point3D location = new Point3D(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				Color pIntensity = new Color(Float.parseFloat(tokens[4]), Float.parseFloat(tokens[5]),Float.parseFloat(tokens[6]));
				int falloff = 0;
				if (tokens.length == 8) {
					falloff = Integer.parseInt(tokens[7]);
				}
				this.plights.add(new PointLight(location, pIntensity, falloff));				
				break;
				
			case "ltd":
				if (tokens.length != 7) {
					System.out.println("ERROR. Line should have 7 tokens. Currently has " + tokens.length);
				}
				Vector3D direction = new Vector3D(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				Color dIntensity = new Color(Float.parseFloat(tokens[4]), Float.parseFloat(tokens[5]),Float.parseFloat(tokens[6]));
				this.dlights.add(new DirectionalLight(direction, dIntensity));
				break;
				
			case "lta":
				if (tokens.length != 4) {
					System.out.println("ERROR. Line should have 4 tokens. Currently has " + tokens.length);
				}
				Color aIntensity = new Color(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				this.alights.add(new AmbientLight(aIntensity));
				break;
				
			case "mat":
				if (tokens.length != 14) {
					System.out.println("ERROR. Line should have 14 tokens. Currently has " + tokens.length);
				}
				Color ka = new Color(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				Color kd = new Color(Float.parseFloat(tokens[4]), Float.parseFloat(tokens[5]), Float.parseFloat(tokens[6]));
				Color ks = new Color(Float.parseFloat(tokens[7]), Float.parseFloat(tokens[8]), Float.parseFloat(tokens[9]));
				int v = Integer.parseInt(tokens[10]);
				Color kr = new Color(Float.parseFloat(tokens[11]), Float.parseFloat(tokens[12]), Float.parseFloat(tokens[13]));
				this.currentMaterial = new Material(ka, kd, ks, v, kr);
				break;
				
			case "xft":
				if (tokens.length != 4) {
					System.out.println("ERROR. Line should have 4 tokens. Currently has " + tokens.length);
				}
				Matrix translation = Transformation.createTranslation(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				Object[] transArr = {"xft", translation};
				this.transformationAndMatrix.add(transArr);
				break;
				
			case "xfr":
				if (tokens.length != 4) {
					System.out.println("ERROR. Line should have 4 tokens. Currently has " + tokens.length);
				}
				Matrix rotation = Transformation.createRotation(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				Object[] rotateArr = {"xfr", rotation};
				this.transformationAndMatrix.add(rotateArr);
				break;
				
			case "xfs":
				if (tokens.length != 4) {
					System.out.println("ERROR. Line should have 4 tokens. Currently has " + tokens.length);
				}
				Matrix scale = Transformation.createScale(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				Object[] scaleArr = {"xfs", scale};
				this.transformationAndMatrix.add(scaleArr);
				break;
				
			case "xfz":
				if (tokens.length != 1) {
					System.out.println("ERROR. Line should have 1 token. Currently has " + tokens.length);
				}
				this.transformationAndMatrix = new ArrayList<Object[]>();
				break;
				
			default: 
				System.out.println("Unrecognized line format. Line ignored.");
				break;
			
		}
		
	}
	private Sphere[] getSpheres() {
		Sphere[] s = new Sphere[spheres.size()];
		return spheres.toArray(s);
	}
	
	private Triangle[] getTriangles() {
		Triangle[] t = new Triangle[triangles.size()];
		return triangles.toArray(t);
	}
	public ObjectContainer getAllObjects() {
		return new ObjectContainer(getSpheres(), getTriangles());
	}
	
	public LightContainer getLights() {
		DirectionalLight[] d = new DirectionalLight[dlights.size()];
		AmbientLight[] a = new AmbientLight[alights.size()];
		PointLight[] p = new PointLight[plights.size()];
		return new LightContainer(dlights.toArray(d), plights.toArray(p), alights.toArray(a));
	}
	
	public Camera getCamera() {
		return cam;
	}
	
}