// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

// Figure 1.5 and 1.6 of small.df
public class Point {
  private float x, y;
  public Point(float _x, float _y){
    x = _x;
    y = _y;
  }

  public float getX(){
    return x;
  }

  public float getY(){
    return y;
  }
}
