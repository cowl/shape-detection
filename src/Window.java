// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Window extends JFrame {

  public Window(Visualisation v, int JFrameOnExit) {
    setTitle("Visualisation");
    setDefaultCloseOperation(JFrameOnExit);
    add(v);
    setSize(800, 800);
    setLocationRelativeTo(null);
    setVisible(true);
  }

}
