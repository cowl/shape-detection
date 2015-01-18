// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.util.*;

public class Main {

  public static List<Point> pts = (new PixelExtractor()).Process("../img/fig1-5.png");

  public static void main(String[] args){
    System.out.println(pts.size());
  }
}
