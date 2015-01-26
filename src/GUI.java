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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;


public class GUI extends JFrame implements Action {
	public static BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
	public static List<Point2D> pts = null;
	public static ImagePreprocessor<List<Point2D>> img = null;
	public String filepath = "img/fig1-5.png";
	public JTextField pathField;
	
	public GUI(){
		this.setTitle("RANSAC by Tom and Niels");
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(300, 300);
	   
	    JPanel mainPane = new JPanel();
	    mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
	    mainPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    pathField = new JTextField(200);
	    pathField.setActionCommand("filepath");
	    pathField.addActionListener(this);
	    mainPane.add(pathField);
	    
	    JButton findPath = new JButton("Set source file");
	    findPath.setActionCommand("findpath");
	    findPath.setAlignmentX(CENTER_ALIGNMENT);
	    findPath.addActionListener(this);
	    mainPane.add(findPath);

	    JButton runBtn = new JButton("Run RANSAC");
	    runBtn.setActionCommand("run");
	    runBtn.setAlignmentX(CENTER_ALIGNMENT);
	    runBtn.addActionListener(this);
	    mainPane.add(runBtn);
	    
	    Container contentPane = getContentPane();
	    //contentPane.add(listPane, BorderLayout.CENTER);
	    contentPane.add(mainPane, BorderLayout.PAGE_START);
	    
		setVisible(true);
	}
	
	public void RunRANSAC(){
	    try{
	        img = new PixelExtractor(filepath);
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
	      new Window(viz, JFrame.DISPOSE_ON_CLOSE);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		 if ("run".equals(e.getActionCommand())) {
			 RunRANSAC();
		 }
		 else if("filepath".equals(e.getActionCommand())){
			    filepath = pathField.getText();
		 }
		 else if("findpath".equals(e.getActionCommand())){
			  JFileChooser chooser = new JFileChooser();
			  chooser.setCurrentDirectory(new java.io.File("."));
			  chooser.setDialogTitle("choosertitle");
			  chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			  chooser.setAcceptAllFileFilterUsed(false);
			  if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				  filepath = chooser.getSelectedFile().toString();
				  pathField.setText(filepath);
			  }
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
