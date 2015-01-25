import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;


public class GUI extends JFrame implements Action {
	public static BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
	public static List<Point2D> pts = null;
	public static ImagePreprocessor<List<Point2D>> img = null;
	JButton runBtn;
	
	public GUI(){
		this.setTitle("RANSAC by Tom and Niels");
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(300, 300);
	    
	    JPanel buttonPane = new JPanel();
	    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.PAGE_AXIS));
	    buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	    buttonPane.add(Box.createHorizontalGlue());
	    runBtn = new JButton("Run RANSAC");
	    runBtn.setActionCommand("run");
	    runBtn.addActionListener(this);
	    buttonPane.add(runBtn);
	    
	    Container contentPane = getContentPane();
	    //contentPane.add(listPane, BorderLayout.CENTER);
	    contentPane.add(buttonPane, BorderLayout.PAGE_END);
	    
		setVisible(true);
	}
	
	public void RunRANSAC(){
	    try{
	        img = new PixelExtractor("img/fig1-5.png");
	        pts = img.getOutput();
	        System.out.println("Number of points: " + pts.size());
	      } catch(Exception e){
	        System.out.println("Cannot find source image.");
	      }


	      RANSAC alg = new RANSAC();

	      alg.Run(new CircleModel(), pts, 5, 0.5f, 100, 1000);

	      Visualisation viz = new Visualisation();
	      viz.setBackground(img.getImage());
	      viz.setPoints(img.getOutput());
	      viz.setAnnulus(new Circle2D(100,100,50), 50);

	      viz.setPoints(alg.getTop().getInliers());
	      System.out.println("Number of steps done: " + alg.getnStep());
	      System.out.println("Number of inliers: " + alg.getTop().score);
	      new Window(viz);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		 if ("run".equals(e.getActionCommand())) {
			 RunRANSAC();
		 }
		
	}
	
	@Override
	public Object getValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void putValue(String key, Object value) {
		// TODO Auto-generated method stub
		
	}
	
}
