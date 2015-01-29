import java.util.List;
import java.util.Random;

import math.geom2d.Point2D;

/**
 * Body of the RANSAC algorithm
 * 
 * @author Tom
 *
 */
public class RANSAC {
	// Probability that the random sample points are all inliers
	private double probability;
	// Threshold under which are points considered inliers
	private double threshold;
	// Number of iteration of the algorithm
	private int nStep;
	// Percentage of outliers in whole set
	private double eRatio;
	
	// The best model
	private Model top;
	
	// RANSAC run - first with given number of steps estimating outliers ratio
	// second run uses the estimation to count number of steps needed
	public Model Run(Model prototype, List<Point2D> pts, double threshold, double probability, int nStepInit, int nMax){
		this.threshold = threshold;
		this.probability = probability;

		top = FindTop(prototype, pts, threshold, nStepInit);
		eRatio = ((double)pts.size() - top.countCardinalityScore()) / (double)pts.size();
		nStep = (int)(Math.log(1f - probability)/Math.log(1 - Math.pow((1 - eRatio), prototype.getSampleNeeded())));
		if(nStep > nMax) nStep = nMax;
		top = FindTop(prototype, pts, threshold, nStep);
		
		return top;
	}

	private Model FindTop(Model prototype, List<Point2D> pts, double threshold, int nStep){
		Model model = prototype.clone();
		top = prototype.clone();
		Point2D [] samples;
		Random rand;
		for(int i = 0; i < nStep; i++){
		    samples = new Point2D [model.getSampleNeeded()];
		    rand = new Random();
		    do{
		    	for(int j = 0; j < model.getSampleNeeded(); j++){
		    		samples[j] = pts.get(rand.nextInt(pts.size()));
		    	}
		    }while(!model.setParameters(samples));
		    model.setInliers(pts, threshold);
		    model.countCardinalityScore();
		    if(model.getScore() > top.getScore())top = model.clone();
		}
		return top;
	}
	
	// Finds models with different sizes
	public CircleModel [] FindTopCircles(CircleModel prototype, List<Point2D> pts, double threshold, double probability, int nStepInit, int nMax, int topX, double areaSize){
		this.threshold = threshold;
		this.probability = probability;
		CircleModel [] topC = new CircleModel[topX];

		top = FindTop(prototype, pts, threshold, nStepInit);
		double eRatio = ((double)pts.size() - top.countCardinalityScore()) / (double)pts.size();
		nStep = (int)(Math.log(1f - probability)/Math.log(1 - Math.pow((1 - eRatio), prototype.getSampleNeeded())));
		if(nStep > nMax) nStep = nMax;
		
		double minRadius = areaSize/2f, maxRadius = areaSize;
		for(int i = 0; i < topX; i++){
			topC[i] = FindTopCircle(prototype, pts, threshold, nStep, minRadius, maxRadius);
			maxRadius = minRadius;
			minRadius = minRadius/2f;
		}
		top = topC[0];
		return topC;
	}

	private CircleModel FindTopCircle(CircleModel prototype, List<Point2D> pts, double threshold, int nStep, double minRadius, double maxRadius){
		CircleModel model = prototype.clone();
		CircleModel top = prototype.clone();
		Point2D [] samples;
		Random rand;
		for(int i = 0; i < nStep; i++){
		    samples = new Point2D [model.getSampleNeeded()];
		    rand = new Random();
		    do{
		    	for(int j = 0; j < model.getSampleNeeded(); j++){
		    		samples[j] = pts.get(rand.nextInt(pts.size()));
		    	}
		    }while(!model.setParameters(samples) || model.getCircle().radius() > maxRadius || model.getCircle().radius() < minRadius);
		    model.setInliers(pts, threshold);
		    model.countCardinalityScore();
		    if(model.getScore() > top.getScore())top = model.clone();
		}
		return top;
	}
	
	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public int getnStep() {
		return nStep;
	}

	public void setnStep(int nStep) {
		this.nStep = nStep;
	}

	public Model getTop() {
		return top;
	}
	
	public double getRatio() {
		return eRatio;
	}
}
