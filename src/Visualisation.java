// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import math.geom2d.Point2D;

public class Visualisation extends JPanel {

  private BufferedImage background = null;
  private java.util.List<Point2D> points = null;

  @Override
  public void paintComponent(Graphics g){
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    if(background != null){
      g2d.drawImage(background, null, 0, 0);
    }

    if(points != null){
      g2d.setColor(Color.red);
      for(Point2D p : points){
        int x = (int)p.getX(), y = (int)p.getY();
        g2d.drawLine(x, y, x, y);
      }
    }

  }

  public void setBackground(BufferedImage img){
    background = img;
  }

  public void setPoints(java.util.List<Point2D> pts){
    points = pts;
  }

}
