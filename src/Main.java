// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.io.*;
import java.util.*;

import math.geom2d.Point2D;

public class Main {

  public static BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
  public static List<Point2D> pts = null;
  public static ImagePreprocessor<List<Point2D>> img = null;

  public static void main(String[] args){
    try{
      img = new PixelExtractor("img/fig1-5.png");
      pts = img.getOutput();
      System.out.println(pts.size());
    } catch(Exception e){ System.out.println("uh..."); }


    RANSAC alg = new RANSAC(1000, 10);
    
    alg.Run(pts, new CircleModel());
    
    Visualisation viz = new Visualisation();
    viz.setBackground(img.getImage());
    //viz.setPoints(img.getOutput());
    
    viz.setPoints(alg.getTop().getInliers());
    System.out.println(alg.getTop().score);
    new Window(viz);

  }

}
