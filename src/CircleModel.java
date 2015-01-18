import java.util.ArrayList;
import java.util.Iterator;
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
	
	public CircleModel(){
		super();
	};
	
	public CircleModel(CircleModel other){
		super();
		circle = other.getCircle();
		Iterator<Point2D> iterator = other.getInliers().iterator();
		while(iterator.hasNext()){
			inliers.add(iterator.next());
		}
		score = other.getScore();
	}
	
	@Override
	public boolean setParameters(Point2D[] samples) {
		if(samples.length < 3){
			return false;
		}
		else if(samples[0].contains(samples[1]) || samples[0].contains(samples[2]) || samples[1].contains(samples[2])){
			// Samples given should not be the same points
			return false;
		}
		circle = Circle2D.circumCircle(samples[0], samples[1], samples[2]);
		return true;
	}
	
	@Override
	public CircleModel clone(){
		return new CircleModel(this);
	}
	

	public double countScore(List<Point2D> pts, double threshold){
		if(circle == null || pts == null || pts.size() == 0){
			return 0;
		}
		inliers = new ArrayList<Point2D>();
	
		Point2D point;
		Iterator<Point2D> iterator = pts.iterator();
		while(iterator.hasNext()){
			point = iterator.next();
			if(circle.distance(point) < threshold){
				inliers.add(point);
			}
		}
		
		score = (double)inliers.size()/(double)pts.size();
		
		return score;
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
