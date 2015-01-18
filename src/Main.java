// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.io.*;
import java.util.*;

public class Main {

  public static BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
  public static List<Point2D> pts = null;
  public static ImagePreprocessor<List<Point2D>> img = null;

  public static void main(String[] args){
    try{
      img = new PixelExtractor("../img/fig1-5.png");
      pts = img.getOutput();
      System.out.println(pts.size());
    } catch(Exception e){ System.out.println("uh..."); }

    Visualisation viz = new Visualisation();
    viz.setBackground(img.getImage());
    viz.setPoints(img.getOutput());
    new Window(viz);

  }

}
