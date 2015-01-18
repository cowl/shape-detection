// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public abstract class ImagePreprocessor<T> {

  protected BufferedImage source;
  protected T output;

  public ImagePreprocessor(String filename) throws IOException {
    this(new File(filename));
  }
  public ImagePreprocessor(File file) throws IOException {
    source = ImageIO.read(file);
    output = process();
  }

  protected abstract T process();

  public BufferedImage getImage(){
    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
    Graphics g = b.getGraphics();
    g.drawImage(source, 0, 0, null);
    g.dispose();
    return b;
  }

  public T getOutput(){
    return output;
  }

}
