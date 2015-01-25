import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import math.geom2d.conic.Circle2D;
import math.geom2d.line.Line2D;
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
			// Some of the points are the same
			return false;
		}
		else if((new Line2D(samples[0], samples[1])).contains(samples[2])){
			// All three are in same line
			return false;
		}
		try{
			circle = Circle2D.circumCircle(samples[0], samples[1], samples[2]);
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
	
	@Override
	public CircleModel clone(){
		return new CircleModel(this);
	}
	
	public void setInliers(List<Point2D> pts, double threshold){
		if(circle == null || pts == null || pts.size() == 0){
			return;
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
