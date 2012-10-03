package test;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*; 


public class FrontEnd extends JPanel
	   implements ActionListener {
	   JButton go;
	   JButton detect;
	   public static String selectedFName="";
	   JFileChooser chooser;
	   String choosertitle="Choose..";
	   JLabel picLabel=null;
	  public  FrontEnd() {
	    go = new JButton("Choose File");
	   // go.setVerticalTextPosition(AbstractButton.BOTTOM);
	   // go.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
	    go.addActionListener(this);
	    add(go);
	    //return selectedFName;
	   }
	  
	  public void actionPerformed(ActionEvent e) {
	    int result;
	    chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle(choosertitle);
	    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    //
	    // disable the "All files" option.
	    //
	    chooser.setAcceptAllFileFilterUsed(false);
	    //    
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
	      System.out.println("getCurrentDirectory(): "
	         +  chooser.getCurrentDirectory());
	      System.out.println("getSelectedFile() : "
	         +  chooser.getSelectedFile());
	      
	      FaceDetect.selectedFileName=chooser.getSelectedFile().toString();
	      result=1;
	    
		if(picLabel!=null){
	      FaceDetect.panelOne.remove(picLabel);
	      
	      FaceDetect.panelOne.validate();
	      FaceDetect.panelOne.repaint();
	      if(FaceDetect.resultPicLabel!=null){
      	      FaceDetect.panelTwo.remove(FaceDetect.resultPicLabel);
      	      FaceDetect.panelTwo.repaint();
      	      FaceDetect.panelTwo.validate();
      		}
		}
	      picLabel= new JLabel (FaceDetect.createImageLabel(FaceDetect.selectedFileName));
	      FaceDetect.panelOne.add( picLabel);
	      FaceDetect.panelOne.validate();
	    }
	    else {
	      System.out.println("No Selection ");
	      }
	     }
	    
	  public Dimension getPreferredSize(){
	    return new Dimension(200, 200);
	    }
}
