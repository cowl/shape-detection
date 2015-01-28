// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import javax.swing.JFileChooser;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;

import math.geom2d.Point2D;

public class Main {

  public static void main(String[] args){
	  /*	int n = 6;
		double w = 3.66f,
				e = (3.95f - w) / w,
				ro = 0.076f,
				areaSize = 802.6,
				pi2 = 2 * Math.PI;
		BigDecimal a0 = new BigDecimal(pi2).pow(n-1),
				a1 = new BigDecimal(ro).pow(n),
				a2 = new BigDecimal(w).pow(2*n-2),
				a3 = new BigDecimal(e).pow(n-2),
				a4 = new BigDecimal(areaSize),
				eAnnuli;
		eAnnuli = (a0.multiply(a1).multiply(a2).multiply(a3).multiply(a4)).divide(Annulus.fac(new BigDecimal(n-2)), BigDecimal.ROUND_HALF_EVEN);
		System.out.println("a0: " + a0);
		System.out.println("a1: " + a1);
		System.out.println("a2: " + a2);
		System.out.println("a3: " + a3);
		System.out.println("a4: " + a4);
		
		System.out.println("Annulus.fac(new BigDecimal(n-2)): " + Annulus.fac(new BigDecimal(n-2)));
		System.out.println("eAnnuli: " + eAnnuli.doubleValue());*/
	  
	  
   new GUI();
  }

}
