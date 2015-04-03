package utility;

import Jama.Matrix;

public class Transformation {

	
	public static Matrix createTranslation(float x, float y, float z ){
		double[] a = {1, 0, 0, x};
    	double[] b = {0, 1, 0, y};
    	double[] c = {0, 0, 1, z};
    	double[] d = {0, 0, 0, 1};
    	
    	double[][] array = {a, b, c, d};
    	Matrix A = new Matrix(array);
    	return A;
	}
	
	public static Matrix createRotation(float x, float y, float z ){
		double[] a1 = {1, 0, 0, 0};
    	double[] b1 = {0, Math.cos(Math.toRadians(x)), -Math.sin(Math.toRadians((double) x)), 0};
    	double[] c1 = {0, Math.sin(Math.toRadians(x)), Math.cos(Math.toRadians((double) x)), 0};
    	double[] d1 = {0, 0, 0, 1};
    	double[][] array = {a1, b1, c1, d1};
    	Matrix X = new Matrix(array);
    
		double[] a2 = {Math.cos(Math.toRadians(y)), 0, Math.sin(Math.toRadians(y)), 0};
    	double[] b2 = {0, 1, 0,0};
    	double[] c2 = {-Math.sin(Math.toRadians(y)), 0, Math.cos(Math.toRadians((double) y)), 0};
    	double[] d2 = {0, 0, 0, 1};
    	double[][] array2 = {a2, b2, c2, d2};
    	Matrix Y = new Matrix(array2);
    	
		double[] a3 = {Math.cos(Math.toRadians(z)), -Math.sin(Math.toRadians(z)), 0, 0};
    	double[] b3 = {Math.sin(Math.toRadians(z)), Math.cos(Math.toRadians(z)), 0, 0};
    	double[] c3 = {0, 0, 1,0};
    	double[] d3 = {0, 0, 0, 1};
    	double[][] array3 = {a3, b3, c3, d3};
    	Matrix Z = new Matrix(array3);
       return Z.times(Y).times(X);
	}
	
	public static Matrix createScale(float x, float y, float z ){
		double[] a = {x, 0, 0, 0};
    	double[] b = {0, y, 0, 0};
    	double[] c = {0, 0, z, 0};
    	double[] d = {0, 0, 0, 1};
    	double[][] array = {a, b, c, d};
    	
    	Matrix A = new Matrix(array);
    	return A;
	}
}