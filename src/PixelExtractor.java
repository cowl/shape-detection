// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.util.*;
import java.awt.image.BufferedImage;

public class PixelExtractor extends ImagePreprocessor<List<Point>> {

  private static int treshold = 0x40; // grayscale value (0-0xFF) below which the pixel is considered 'a point'

  public List<Point> Process(BufferedImage img){
    List<Point> pts = new ArrayList<Point>();
    int w = img.getWidth(), h = img.getHeight();
    for(int x = 0; x < w; x++)
      for(int y = 0; y < w; y++){
        int rgb = img.getRGB(x, y),
            r   = rgb >> 16 & 0xFF,
            g   = rgb >>  8 & 0xFF,
            b   = rgb >>  0 & 0xFF;
        if(r+g+b < treshold * 3)
          pts.add(new Point(x, y));
      }
    return pts;
  }

}
