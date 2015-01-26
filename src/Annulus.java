// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.util.*;
import java.math.BigDecimal;
import math.geom2d.conic.Circle2D;
import math.geom2d.Point2D;

public class Annulus {
	private List<Point2D> pts;
	private Circle2D circle;
	private double e; // width of the annulus
	private int n; // number of points in support set,

	private static int m; // number of points in area
	private static int areaSize; // estimated size of the area
	private static double ro; // density


	// Using the terminology of the slides
	private double width(){
		return circle.radius()-e/2;
	}
	private double thickness(){
		return e;
	}

	public Annulus(List<Point2D> pts){
		n = pts.size();

		// I have no idea.
	}

	// Probability that given points are covered by an annulus of the given width and thickness
	// The smaller, the stronger the evidence
	public double Q(){
		double w = width(), pi2 = Math.PI*Math.PI;
		return n*(e + (n-1)) * Math.pow(pi2, n-1) * Math.pow( w*w/areaSize, n-1 ) * Math.pow(e, n-2);
	}

	// Expected number of annuli of given width and thickness
	// The smaller, the stronger the evidence
	public double E(){
		double w = width(), pi2 = Math.PI*Math.PI;
		BigDecimal a0 = new BigDecimal(pi2).pow(n-1).divide(fac(new BigDecimal(n-2))), a1 = new BigDecimal(ro).pow(n), a2 = new BigDecimal(w).pow(2*n-2), a3 = new BigDecimal(e).pow(n-2), a4 = new BigDecimal(areaSize);
		return (a0.multiply(a1).multiply(a2).multiply(a3).multiply(a4)).doubleValue();
	}

	// Set parameters we need
	public static void setParameters(Point2D[] allPoints){
		m = allPoints.length;
		double[] distanceNearestNeighbour = new double[m];
		for(int i = 0; i < m; i++){
			double mindist = Double.MAX_VALUE;
			for(int j = 0; j < m; j++){
				double dist = allPoints[i].distance(allPoints[j]);
				if(dist < mindist)
					mindist = dist;
			}
			distanceNearestNeighbour[i] = mindist;
		}
		Arrays.sort(distanceNearestNeighbour);
		double mediandist = (distanceNearestNeighbour[m/2]+distanceNearestNeighbour[m/2+m%2])/2;

		ro = Math.log(2) / (Math.PI * mediandist*mediandist);
		areaSize = m/ro;
	}

	private static BigDecimal fac(BigDecimal x){
		return x.compareTo(BigDecimal.ZERO) > 0 ? fac(x.subtract(BigDecimal.ONE)).multiply(x) : new BigDecimal(1);
	}
}
