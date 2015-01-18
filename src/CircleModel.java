import java.util.ArrayList;
import java.util.List;

import math.geom2d.conic.Circle2D;
import math.geom2d.Point2D;

/**
 * A model of a circle.
 * 
 * @author Tom
 *
 */
public class CircleModel extends Model {
	private Circle2D circle = null;
	
	@Override
	public boolean setParameters(Point2D[] samples) {
		if(samples.length < 3){
			return false;
		}
		else if(!samples[0].contains(samples[1]) && !samples[0].contains(samples[2]) && !samples[1].contains(samples[2])){
			// Samples given should not be the same points
			return false;
		}
		circle = Circle2D.circumCircle(samples[0], samples[1], samples[2]);
		return true;
	}
	
	// Counts the score and returns the set of inliers
	public List<Point2D> countScore(List<Point2D> data, double threshold){
		List<Point2D> inliers = new ArrayList<Point2D>();
		
		int dataSize = data.size();
		
		
		
		return inliers;
	}
	
	@Override
	public int getSampleNeeded(){
		return 3;
	}
	
	@Override
	public boolean areParametersSet(){
		return circle != null;
	}

	public Circle2D getCircle(){
		return circle;
	}
	
}
