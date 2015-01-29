import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;

/*
 * Main GUI - window where parameters are set
 * 
 */
public class GUI extends JFrame implements Action {
	public static BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
	public static List<Point2D> pts = null;
	public static ImagePreprocessor<List<Point2D>> img = null;
	public String filepath = "img/fig1-5.png", output = "";
	int maxN = 100,initStep = 100, topX = 7;
	double probability = 0.5f, threshold = 5;
	public JTextField pathField, probabilityField, thresholdField, topXField, maxNField, initStepField;
	public JTextArea outputArea;
	
	// Happens after user hits a button
	public void RunRANSAC(){
	    output = "";
		try{
	    	filepath = pathField.getText();
	        img = new PixelExtractor(filepath);
	        pts = img.getOutput();
	      } catch(Exception e){
	    	 output = "There is a tiny problem: \n" + e.toString();
			 outputArea.setText(output);
			 return;
	      }
	    outputArea.setText(output);
	    if(!InputIsAlright()) return;
	    
	      RANSAC alg = new RANSAC();

	      CircleModel [] topC = alg.FindTopCircles(new CircleModel(), pts, threshold, probability, initStep, maxN, topX, img.getImage().getWidth());

	      output = "General info: ";
	      output += "\nNumber of points: " + pts.size();
	      output += "\nEstimated percentage of outliers: " + alg.getRatio() + "\n";
	      
	      Annulus annulus = null;
	      for(int i = 0; i < topC.length; i++){
	    	  output += "\nModel number #" + i;
	    	  output += "\n   Radius: " + topC[i].getCircle().radius();
		      Visualisation viz = new Visualisation();
		      viz.setBackground(img.getImage());
		      viz.setPoints(topC[i].getInliers());
		      if(annulus != null){
		    	  annulus = new Annulus(topC[i].inliers, topC[i].getCircle().center(), annulus.density(), annulus.areaSize());
		      }
		      else annulus = new Annulus(pts, topC[i].inliers, topC[i].getCircle().center());
		      if(annulus != null){
		    	  viz.setAnnulus(annulus);
		    	  output += "\n   Thickness: " + annulus.thickness();
		    	  output += "\n   Q(n): " + annulus.Q().doubleValue();
		    	  output += "\n   E(n): " + annulus.E().doubleValue();
		      }

		      new Window(viz, JFrame.DISPOSE_ON_CLOSE).setTitle("Model number #" + i);
	      }
	      outputArea.setText(output);
	}
	
	public GUI(){
		this.setTitle("RANSAC by Tom and Niels");
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(400, 700);
	    this.setLocationByPlatform(true);
	   
	    JPanel mainPane = new JPanel();
	    mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
	    mainPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    mainPane.add(new JLabel("Path to source file:"));
	    pathField = new JTextField(200);
	    pathField.setActionCommand("filepath");
	    pathField.addActionListener(this);
	    mainPane.add(pathField);
	    
	    JButton findPath = new JButton("Browse directories");
	    findPath.setActionCommand("findpath");
	    findPath.setAlignmentX(JButton.LEFT_ALIGNMENT);
	    findPath.addActionListener(this);
	    mainPane.add(findPath);

	    mainPane.add(Box.createRigidArea(new Dimension(5, 10))); // White space
	    
	    mainPane.add(new JLabel("Probability of all samples inliers:"));
	    probabilityField = new JTextField(15);
	    probabilityField.setText(probability + "");
	    probabilityField.setActionCommand("setProbability");
	    probabilityField.addActionListener(this);
	    mainPane.add(probabilityField);
	    
	    mainPane.add(Box.createRigidArea(new Dimension(5, 10))); // White space
	    
	    mainPane.add(new JLabel("Threshold for being an inlier:"));
	    thresholdField = new JTextField(15);
	    thresholdField.setText(threshold + "");
	    thresholdField.setActionCommand("setThreshold");
	    thresholdField.addActionListener(this);
	    mainPane.add(thresholdField);
	    
	    mainPane.add(Box.createRigidArea(new Dimension(5, 10))); // White space
	    
	    mainPane.add(new JLabel("Number of the best models:"));
	    topXField = new JTextField(15);
	    topXField.setText(topX + "");
	    topXField.setActionCommand("setTopX");
	    topXField.addActionListener(this);
	    mainPane.add(topXField);
	    
	    mainPane.add(Box.createRigidArea(new Dimension(5, 10))); // White space
	    
	    mainPane.add(new JLabel("Maximum steps done:"));
	    maxNField = new JTextField(15);
	    maxNField.setText(maxN + "");
	    maxNField.setActionCommand("setMaxN");
	    maxNField.addActionListener(this);
	    mainPane.add(maxNField);
	    
	    mainPane.add(Box.createRigidArea(new Dimension(5, 10))); // White space
	    
	    mainPane.add(new JLabel("Initial number of steps for estimation:"));
	    initStepField = new JTextField(15);
	    initStepField.setText(initStep + "");
	    initStepField.setActionCommand("setInitStep");
	    initStepField.addActionListener(this);
	    mainPane.add(initStepField);
	    
	    mainPane.add(Box.createRigidArea(new Dimension(5, 15))); // White space
	    
	    JButton runBtn = new JButton("Run RANSAC");
	    runBtn.setActionCommand("run");
	    runBtn.setAlignmentX(JButton.LEFT_ALIGNMENT);
	    runBtn.addActionListener(this);
	    mainPane.add(runBtn);
	    
	    mainPane.add(Box.createRigidArea(new Dimension(5, 15))); // White space
	    
        outputArea = new JTextArea();
        outputArea.setColumns(20);
        outputArea.setLineWrap(true);
        outputArea.setRows(18);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        outputArea.setText("Welcome! \nPlease set the parameters and we can roll on. \n\nIt may take a while, but we do our best");
        mainPane.add(outputArea);
       
        JScrollPane jScrollPane = new JScrollPane(outputArea);
        mainPane.add(jScrollPane);

	    
	    Container contentPane = getContentPane();
	    //contentPane.add(listPane, BorderLayout.CENTER);
	    contentPane.add(mainPane, BorderLayout.PAGE_START);
	    
		setVisible(true);
	}
	
	// Button, text field etc. listener
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
	
	public boolean InputIsAlright(){
		boolean alright = true;
		String tmpStr;
		double tmpDouble;
		int tmpInt;

		 tmpStr = probabilityField.getText();
		 output = "Setting the probability...";
		 try{
			 tmpDouble = Double.parseDouble(tmpStr);
			 if(tmpDouble > 0 && tmpDouble < 1){
				 output += "\nOK";
				 probability = tmpDouble;
			 }
			 else{
				 alright = false;
				 output += "\nProbability has to be in the interval (0; 1)";
			 }
		 }catch(Exception ex){
			 alright = false;
			 output += "\nThere is a tiny problem: \n" + ex.toString();
		 }

		 tmpStr = thresholdField.getText();
		 output += "\n\nSetting the threshold...";
		 try{
			 tmpDouble = Double.parseDouble(tmpStr);
			 if(tmpDouble > 0){
				 output += "\nOK";
				 threshold = tmpDouble;
			 }
			 else{
				 alright = false;
				 output += "\nThreshold has to be positive";
			 }
		 }catch(Exception ex){
			 alright = false;
			 output += "\nThere is a tiny problem: \n" + ex.toString();
		 }
		 
		 tmpStr = topXField.getText();
		 output += "\n\nSetting the number of models...";
		 try{
			 tmpInt = Integer.parseInt(tmpStr);
			 if(tmpInt > 0 && tmpInt < 10){
				 output += "\nOK";
				 topX = tmpInt;
			 }
			 else{
				 alright = false;
				 output += "\nNumber of models has to be in the interval (0; 10>";
			 }
		 }catch(Exception ex){
			 alright = false;
			 output += "\nThere is a tiny problem: \n" + ex.toString();
		 }
		 
		 tmpStr = maxNField.getText();
		 output += "\n\nSetting the max number of steps...";
		 try{
			 tmpInt = Integer.parseInt(tmpStr);
			 if(tmpInt > 0){
				 output += "\nOK";
				 maxN = tmpInt;
			 }
			 else{
				 alright = false;
				 output += "\nNumber steps has to be positive";
			 }
		 }catch(Exception ex){
			 alright = false;
			 output += "\nThere is a tiny problem: \n" + ex.toString();
		 }
		 
		 tmpStr = initStepField.getText();
		 output += "\n\nSetting the initial number of steps...";
		 try{
			 tmpInt = Integer.parseInt(tmpStr);
			 if(tmpInt > 0){
				 output += "\nOK";
				 initStep = tmpInt;
			 }
			 else{
				 alright = false;
				 output += "\nNumber steps has to be positive";
			 }
		 }catch(Exception ex){
			 alright = false;
			 output += "\nThere is a tiny problem: \n" + ex.toString();
		 }
		
		 outputArea.setText(output);
		 
		return alright;
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
