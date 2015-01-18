// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public abstract class ImagePreprocessor<T> {

  protected BufferedImage original, image;
  protected T output;

  public ImagePreprocessor(String filename) throws IOException {
    this(new File(filename));
  }
  public ImagePreprocessor(File file) throws IOException {
    image = original = ImageIO.read(file);
    output   = process();
  }

  protected abstract T process();

  public BufferedImage getOriginal(){
    return original;
  }

  public BufferedImage getImage(){
    return image;
  }

  public T getOutput(){
    return output;
  }

}
