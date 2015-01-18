// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.util.*;
import java.io.*;
import java.awt.image.BufferedImage;

public class PixelExtractor extends ImagePreprocessor<List<Point>> {

  public PixelExtractor(String filename) throws IOException {
    super(filename);
  }

  // Grayscale value (0-0xFF) below which the pixel is considered 'a point'
  // Note that we could also have preprocessed the buffer image using
  // BufferedImage.TYPE_BYTE_GRAY or TYPE_BYTE_BINARY and simply taken the R val
  private static int treshold = 0x40;

  protected List<Point> process(){
    List<Point> pts = new ArrayList<Point>();
    int w = image.getWidth(), h = image.getHeight();
    for(int x = 0; x < w; x++)
      for(int y = 0; y < w; y++){
        int rgb = image.getRGB(x, y),
            r   = rgb >> 16 & 0xFF,
            g   = rgb >>  8 & 0xFF,
            b   = rgb >>  0 & 0xFF;
        if(r+g+b < treshold * 3)
          pts.add(new Point(x, y));
      }
    return pts;
  }

}
