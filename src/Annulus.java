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
	
	public static Annulus create(List<Point2D> wholeSet, List<Point2D> _supportSet, Point2D center){
		Annulus an = new Annulus(wholeSet, _supportSet, center);
		if(an.getCircle() == null){
			an = null;
		}
		return an;
	}
	
	public static Annulus create(List<Point2D> _supportSet, Point2D center, double _ro, double _areaSize){
		Annulus an = new Annulus(_supportSet, center, _ro, _areaSize);
		if(an.getCircle() == null){
			an = null;
		}
		return an;
	}

	private void Init(List<Point2D> _supportSet, Point2D center){
		supportSet = _supportSet.toArray(new Point2D[0]);
		setCircle(supportSet, center);
		if(circle != null)e = stripeWidth / width();
	}
	
	// To create Annulus we need the support set and its center
	private Annulus(List<Point2D> wholeSet, List<Point2D> _supportSet, Point2D center){
		Init(_supportSet, center);
		setParameters(wholeSet.toArray(new Point2D[0]), wholeSet.size());
	}
	
	private Annulus(List<Point2D> _supportSet, Point2D center, double _ro, double _areaSize){
		Init(_supportSet, center);
		ro = _ro;
		areaSize = _areaSize;
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
		if(supSet.length < 1 || center == null) return;
		double maxDist, minDist;
		maxDist = minDist = center.distance(supSet[0]);
		double dist = 0;
		for(int j = 0; j < supSet.length; j++){
			dist = center.distance(supSet[j]);
			if(dist < minDist){
				minDist = dist;
			}
			if(dist > maxDist){
				maxDist = dist;
			}
		}
		Circle2D minCircle = new Circle2D(center, minDist);
		Circle2D maxCircle = new Circle2D(center, maxDist);
		stripeWidth = Math.abs(minCircle.radius() - maxCircle.radius());
		double radius = minCircle.radius() + stripeWidth/2f;
		circle = new Circle2D(center, radius);

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
	

	public Circle2D getCircle() {
		return circle;
	}

	public double getStripeWidth() {
		return stripeWidth;
	}
	
	public double areaSize() {
		return areaSize;
	}
	
	public double density() {
		return ro;
	}
	
}
