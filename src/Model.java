import java.util.ArrayList;
import java.util.List;

import math.geom2d.Point2D;


/**
 * Represents a model to be fitted by RANSAC.
 * 
 * @author Tom
 *
 */
public abstract class Model {
	protected double score;
	protected List<Point2D> inliers;

	public Model(){
		inliers = new ArrayList<Point2D>();
	}
	
	// The model itself will set its parameters
	public abstract boolean setParameters(Point2D [] samples);
	
	public double countCardinalityScore(){
		score = (double)inliers.size();
		return score;
	}
	
	// Cloning a model
	public abstract Model clone();
	
	// Needed number of samples
	public abstract int getSampleNeeded();
	
	// The model does not have to be initialized
	public abstract boolean areParametersSet();
	
	public abstract void setInliers(List<Point2D> pts, double threshold);
	
	public List<Point2D> getInliers() {
		return inliers;
	}
	
	public double getScore(){
		return score;
	}
}
