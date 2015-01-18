import math.geom2d.Point2D;


/**
 * Represents a model to be fitted by RANSAC.
 * 
 * @author Tom
 *
 */
public abstract class Model {
	protected double score;
	
	// The model itself will set its parameters
	public abstract boolean setParameters(Point2D [] samples);
	
	// Needed number of samples
	public abstract int getSampleNeeded();
	
	// The model does not have to be initialized
	public abstract boolean areParametersSet();
	
	public double getScore(){
		return score;
	}
}
