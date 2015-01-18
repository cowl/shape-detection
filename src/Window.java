// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Window extends JFrame {

  public Window(Visualisation v) {
    setTitle("Visualisation");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(v);
    setSize(800, 800);
    setLocationRelativeTo(null);
    setVisible(true);
  }

}
