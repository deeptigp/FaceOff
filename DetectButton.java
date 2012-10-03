package test;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*; 


public class DetectButton extends JPanel
implements ActionListener {
JButton detect;

public  DetectButton() {
 detect = new JButton ("Detect face");
 detect.addActionListener(this);
 add(detect);
}

public void actionPerformed(ActionEvent e) {
	if(FaceDetect.selectedFileName!="")
	{
		
	}
  }
 
}
