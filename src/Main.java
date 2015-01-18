// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.util.*;
import java.io.*;

public class Main {

  public static BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
  public static List<Point> pts = null;
  //public static BufferedImage img = null;
  public static ImagePreprocessor<List<Point>> img = null;

  public static void main(String[] args){
    try{
      img = new PixelExtractor("../img/fig1-5.png");
      pts = img.getOutput();
      System.out.println(pts.size());
    } catch(Exception e){ System.out.println("uh..."); }

    Window viz = new Window();
    viz.setVisible(true);
  }

}
