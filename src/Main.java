// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.io.*;
import java.util.*;
import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;

import math.geom2d.Point2D;

public class Main {

  public static BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
  public static List<Point2D> pts = null;
  public static ImagePreprocessor<List<Point2D>> img = null;

  public static void main(String[] args){
    try{
      img = new PixelExtractor("img/fig1-5.png");
      pts = img.getOutput();
      System.out.println("Number of points: " + pts.size());
    } catch(Exception e){
      System.out.println("Cannot find source image.");
    }


    RANSAC alg = new RANSAC();

    alg.Run(new CircleModel(), pts, 2, 0.5f, 100, 1000);

    Visualisation viz = new Visualisation();
    viz.setBackground(img.getImage());
    viz.setPoints(img.getOutput());
    viz.setAnnulus(new Circle2D(100,100,50), 50);

    viz.setPoints(alg.getTop().getInliers());
    System.out.println("Number of steps done: " + alg.getnStep());
    System.out.println("Number of inliers: " + alg.getTop().score);
    new Window(viz);

  }

}
