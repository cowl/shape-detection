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
  private Annulus annulus = null;

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
      Point2D c = annulus.getCircle().center();
      int r = (int)annulus.getCircle().radius(), x = (int)c.getX(), y = (int)c.getY();

      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      int w = ((int) annulus.thickness() / 2);
      g2d.setPaint(Color.green);
      g2d.drawOval(x-r+w, y-r+w, (r-w)*2, (r-w)*2);
      g2d.drawOval(x-r-w, y-r-w, (r+w)*2, (r+w)*2);

      g2d.setPaint(Color.blue);
      g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {4}, 0));
      g2d.drawOval(x-r, y-r, r*2, r*2);
    }

    g2d.dispose();
  }

  public void setBackground(BufferedImage _background){
    background = _background;
  }

  public void setPoints(java.util.List<Point2D> _points){
    points = _points;
  }

  public void setAnnulus(Annulus _annulus){
    annulus = _annulus;
  }

}
