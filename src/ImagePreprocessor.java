// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public abstract class ImagePreprocessor<T> {

  public abstract T Process(BufferedImage img);

  public T Process(String filename) throws IOException {
    BufferedImage img = null;
    img = ImageIO.read(new File(filename));
    return Process(img);
  }

}
