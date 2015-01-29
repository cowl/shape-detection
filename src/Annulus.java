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
	private double stripeWidth;
	private double e; // width of the annulus
	private double areaSize; // estimated size of the area
	private double ro; // density

	private BigDecimal coveringProbability = null, eAnnuli = null;

	// Using the terminology of the slides
	public double width(){
		return circle.radius() - stripeWidth/2f;
	}
	
	public double thickness(){
		return e;
	}

	// To create Annulus we need the support set and its center
	public Annulus(List<Point2D> wholeSet, List<Point2D> _supportSet, Point2D center){
		supportSet = _supportSet.toArray(new Point2D[0]);
		setCircle(supportSet, center);
		if(circle != null)e = stripeWidth / width();
		setParameters(wholeSet.toArray(new Point2D[0]), 100);
	}

	// Probability that given points are covered by an annulus of the given width and thickness
	// The smaller, the stronger the evidence
	// Have to use BigDecimal for precision... too many points
	public BigDecimal Q(){
		if(coveringProbability == null){
			double w = width();
			int n = supportSet.length;
			BigDecimal a0 = new BigDecimal(n * (e + (n-1))),
					a1 = new BigDecimal((2 * Math.PI)).pow(n-1),
					a2 = new BigDecimal((w * w)/areaSize).pow(n-1),
					a3 = new BigDecimal(e).pow(n-2);
			coveringProbability = a0.multiply(a1).multiply(a2).multiply(a3);
		}
		return coveringProbability;
	}

	// Expected number of annuli of given width and thickness
	// The smaller, the stronger the evidence
	public BigDecimal E(){
		if(eAnnuli == null){
			double w = width(), pi2 = 2 * Math.PI;
			int n = supportSet.length;
			BigDecimal a0 = new BigDecimal(pi2).pow(n-1),
					a1 = new BigDecimal(ro).pow(n),
					a2 = new BigDecimal(w).pow(2*n-2),
					a3 = new BigDecimal(e).pow(n-2),
					a4 = new BigDecimal(areaSize);
			eAnnuli = (a0.multiply(a1).multiply(a2).multiply(a3).multiply(a4)).divide(fac(new BigDecimal(n-2)), BigDecimal.ROUND_HALF_EVEN);
		}
		return eAnnuli;
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
			stripeWidth = Math.abs(minCircle.radius() - maxCircle.radius());
			double radius = minCircle.radius() + stripeWidth/2f;
			circle = new Circle2D(center, radius);
		}
		catch(Exception e){
			System.out.println("Annulus not created: " + e.getMessage());
		}
	}
	
	// Set parameters density and estimated areasize
	private void setParameters(Point2D [] wholeSet, int medianSampleSize){
		if(medianSampleSize < 0 || medianSampleSize > wholeSet.length){
			medianSampleSize = wholeSet.length;
		}
	    int m = wholeSet.length;
	    Point2D [] medianSampleSet = new Point2D[medianSampleSize];
		Random rand = new Random();
		for(int i = 0; i < medianSampleSize; i++){
			medianSampleSet[i] = wholeSet[rand.nextInt(m)];
		}
		
		double[] distanceNearestNeighbour = new double[medianSampleSize];
		double dist;
		for(int i = 0; i < medianSampleSize; i++){
			double mindist = Double.MAX_VALUE;
			for(int j = 0; j < medianSampleSize; j++){
				if(!medianSampleSet[i].contains(medianSampleSet[j])){
					dist = medianSampleSet[i].distance(medianSampleSet[j]);
					if(dist < mindist)
						mindist = dist;
				}
			}
			distanceNearestNeighbour[i] = mindist;
		}
		/*for(int i = 0; i < medianSampleSize; i++){
			System.out.println("i" + i + ": " + distanceNearestNeighbour[i]);
		}*/
		Arrays.sort(distanceNearestNeighbour);
		double medianDist = (distanceNearestNeighbour[medianSampleSize/2] + distanceNearestNeighbour[medianSampleSize/2 + medianSampleSize%2]) / 2f;

		ro = Math.log(2) / (Math.PI * medianDist * medianDist);
		areaSize = m / ro;
	}

	public static BigDecimal fac(BigDecimal x){
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

	public double getStripeWidth() {
		return stripeWidth;
	}
	
}
