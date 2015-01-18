// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;

public class Visualisation extends JPanel {

  private BufferedImage background = null;
  private java.util.List<Point2D> points = null;
  private Circle2D annulus = null;
  private float annulusWidth = 1;

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

    if(annulus != null){
      Point2D c = annulus.center();
      int r = (int)annulus.radius(), x = (int)c.getX(), y = (int)c.getY();

      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      g2d.setPaint(Color.green);
      g2d.drawOval(x-r, y-r, x+r, y+r);
    }

    g2d.dispose();
  }

  public void setBackground(BufferedImage _background){
    background = _background;
  }

  public void setPoints(java.util.List<Point2D> _points){
    points = _points;
  }

  public void setAnnulus(Circle2D _circle, float _width){
    annulus = _circle;
    annulusWidth = _width;
  }

}
