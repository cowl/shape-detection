// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.util.*;
import java.math.BigDecimal;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.Line2D;
import math.geom2d.Point2D;

public class Annulus {
	private Point2D [] supportSet; // support set
	private Circle2D circle;
	private double e; // width of the annulus
	private int n; // number of points in support set,

	// TOM: I changed the type to double from int
	private double areaSize; // estimated size of the area
	private double ro; // density


	// Using the terminology of the slides
	public double width(){
		return circle.radius() - e/2f;
	}
	
	public double thickness(){
		return e;
	}

	// To create Annulus we need the support set and its center
	public Annulus(List<Point2D> _supportSet, Point2D center){
		supportSet = _supportSet.toArray(new Point2D[0]);
		setCircle(supportSet, center);
		setParameters(supportSet);
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

	// Finding the circle defining annulus
	private void setCircle(Point2D [] supSet, Point2D center){
		if(supSet.length < 3 || center == null) return;
		// Find the max circle and min circle through three closest points to the center and three most distant
		Point2D [] minPoints = new Point2D[3];
		Point2D [] maxPoints = new Point2D[3];
		double [] minDist = new double[3];
		double [] maxDist = new double[3];
		// Init min/max Point arrays
		minPoints[0] = minPoints[1] = minPoints[2] = maxPoints[0] = maxPoints[1] = maxPoints[2] = null;
		// Init distances
		minDist[0] = minDist[1] = minDist[2] = Double.MAX_VALUE;
		maxDist[0] = maxDist[1] = maxDist[2] = 0;
		// The goal is to find twice three points which can make a circle
		double dist = 0;
		Point2D candidate = null;
		int indexOfCandidate = 0;
		for(int j = 0; j < supSet.length; j++){
			candidate = supSet[j];
			dist = center.distance(candidate);
			// Min part
			if(dist < minDist[2]){
				// Distance is smaller than the biggest of small
				if(dist < minDist[1]){
					if(dist < minDist[0]){
						indexOfCandidate = 0;
					}
					else{
						indexOfCandidate = 1;
					}
				}
				else{
					indexOfCandidate = 2;
				}
				// Check duplicates and colinearity 
				if(CheckCandidatePoint(minPoints, candidate)){
					// Shift points, include candidate
					IncludeCandidate(center, minPoints, minDist, candidate, indexOfCandidate);
				}
			}
			// Max part
			if(dist > maxDist[2]){
				// Distance is bigger than the smallest of big
				if(dist > maxDist[1]){
					if(dist > maxDist[0]){
						indexOfCandidate = 0;
					}
					else{
						indexOfCandidate = 1;
					}
				}
				else{
					indexOfCandidate = 2;
				}
				// Check duplicates and colinearity 
				if(CheckCandidatePoint(maxPoints, candidate)){
					// Shift points, include candidate
					IncludeCandidate(center, maxPoints, maxDist, candidate, indexOfCandidate);
				}
			}

		}
		try{
			Circle2D minCircle = Circle2D.circumCircle(minPoints[0], minPoints[1], minPoints[2]);
			Circle2D maxCircle = Circle2D.circumCircle(maxPoints[0], maxPoints[1], maxPoints[2]);
			e = Math.abs(minCircle.radius() - maxCircle.radius());
			double radius = minCircle.radius() + e/2f;
			circle = new Circle2D(center, radius);
		}
		catch(Exception e){
			System.out.println("Annulus not created: " + e.getMessage());
		}
	}
	
	// Set parameters density and estimated areasize
	private void setParameters(Point2D [] supSet){
		supportSet = supSet;
		n = supSet.length;
		double[] distanceNearestNeighbour = new double[n];
		for(int i = 0; i < n; i++){
			double mindist = Double.MAX_VALUE;
			for(int j = 0; j < n; j++){
				double dist = supSet[i].distance(supSet[j]);
				if(dist < mindist)
					mindist = dist;
			}
			distanceNearestNeighbour[i] = mindist;
		}
		Arrays.sort(distanceNearestNeighbour);
		double mediandist = (distanceNearestNeighbour[n/2]+distanceNearestNeighbour[n/2+n%2])/2;

		ro = Math.log(2) / (Math.PI * mediandist*mediandist);
		areaSize = n/ro;
	}

	private static BigDecimal fac(BigDecimal x){
		return x.compareTo(BigDecimal.ZERO) > 0 ? fac(x.subtract(BigDecimal.ONE)).multiply(x) : new BigDecimal(1);
	}
	
	private boolean CheckCandidatePoint(Point2D [] threePoints, Point2D candidate){
		if(threePoints.length < 3)return false;
		boolean ret = true;
		if(threePoints[0] != null){
			ret = !threePoints[0].contains(candidate);
			if(ret && threePoints[1] != null){
				ret = !threePoints[1].contains(candidate);
				if(ret){
					ret = !(new Line2D(threePoints[0], threePoints[1])).contains(candidate);
				}
			}
		}
		return ret;
	}
	
	private void IncludeCandidate(Point2D center, Point2D [] threePoints, double [] distances, Point2D candidate, int indexOfCandidate){
		Point2D in = candidate, out = null;
		for(int i = indexOfCandidate; i < 3; i++){
			out = threePoints[i];
			threePoints[i] = in;
			if(in != null){
				distances[i] = center.distance(in);
			}
			in = out;
		}
	}

	public Circle2D getCircle() {
		return circle;
	}
}
