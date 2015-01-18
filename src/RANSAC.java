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
	private double prob;
	// Threshold under which are points considered inliers
	private double thresh;
	// Number of iteration of the algorithm
	private int nStep;
	
	// The best model
	private Model top;
	
	public RANSAC(int _nStep, double _tresh){
		nStep = _nStep;
		thresh = _tresh;
	}
	
	public Model Run(List<Point2D> pts, Model prototype){
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
		    model.countScore(pts, thresh);
		    if(model.getScore() > top.getScore())top = model.clone();
		}
		
		return top;
	}
	
	public double getProb() {
		return prob;
	}

	public void setProb(double prob) {
		this.prob = prob;
	}

	public double getTresh() {
		return thresh;
	}

	public void setTresh(double tresh) {
		this.thresh = tresh;
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
}
