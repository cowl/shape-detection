// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.util.*;
import java.io.*;

public class Main {

  public static List<Point> pts = null;

  public static void main(String[] args){
    System.out.println("Enter image path:");
    Scanner scan = new Scanner(System.in);
    String imgPath = scan.next();
    scan.close();

    if(imgPath == "")
      imgPath = "../img/fig1-5.png";

    System.out.println("Image path is:");
    System.out.println(imgPath);

    try {
      pts = (new PixelExtractor()).Process("../img/fig1-5.png");
      System.out.println(pts.size());
    } catch(IOException e){
      System.out.println("An error occurred.");
    }
  }

}
