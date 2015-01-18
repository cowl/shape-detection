// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.awt.image.*;
import java.io.*;
import java.util.*;

public class PixelExtractor extends ImagePreprocessor<List<Point2D>> {

  public PixelExtractor(String filename) throws IOException {
    super(filename);
  }

  // Grayscale value (0-0xFF) below which the pixel is considered 'a point'
  // Note that we could also have preprocessed the buffer image using
  // BufferedImage.TYPE_BYTE_GRAY or TYPE_BYTE_BINARY and simply taken the R val
  private static int treshold = 0xA0;

  @Override
  protected List<Point2D> process(){
    List<Point2D> pts = new ArrayList<Point2D>();
    int w = source.getWidth(), h = source.getHeight();
    for(int x = 0; x < w; x++)
      for(int y = 0; y < w; y++){
        int rgb = source.getRGB(x, y),
            r   = rgb >> 16 & 0xFF,
            g   = rgb >>  8 & 0xFF,
            b   = rgb >>  0 & 0xFF;
        if(r+g+b < treshold * 3)
          pts.add(new Point2D(x, y));
      }
    return pts;
  }

}
