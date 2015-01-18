// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.util.*;
import java.io.*;

public class Main {

  public static BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
  public static List<Point> pts = null;

  public static void main(String[] args){
    try {
      pts = (new PixelExtractor()).Process("../img/fig1-5.png");
      System.out.println(pts.size());
    } catch(IOException e){
      System.out.println("An error occurred.");
    }

  }

}
