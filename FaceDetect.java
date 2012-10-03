package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.googlecode.javacv.*;
import com.googlecode.javacv.cpp.*;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacpp.Loader;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;



import javax.swing.*; 


public class FaceDetect {
	public static ArrayList<String> fileNames= new ArrayList<String>(30);
	public static String selectedFileName;
	public static JFrame frame;
	public static JPanel panelOne,panelTwo;
	public static JButton detect;
	public static JLabel resultPicLabel=null;
	public static JLabel resultFaceConfidence=null;
	public static void listFilesForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	          //  System.out.println(fileEntry.getName());
	        	if(fileEntry.getName().endsWith(".jpeg"))
	        		fileNames.add("profile_pics/new/"+fileEntry.getName());
	        }
	    }
	}	
	
	public static ImageIcon createImageLabel(String fileName)
	{
		  BufferedImage myPicture=null;
			try {
				myPicture = ImageIO.read(new File(fileName));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return new ImageIcon(myPicture);

	}
	
	public static int faceDetect(String imageFileName)
	{
		boolean readFromFolder=false;
		if(readFromFolder)
		{
			final File folder = new File("/Users/pghadiya/Documents/workspace2/FaceDetect/profile_pics/new");
	    	FaceDetect.listFilesForFolder(folder); 
	    	FileWriter fstream=null;
			try {
				fstream = new FileWriter("face_coordinates.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	BufferedWriter out = new BufferedWriter(fstream);
		}  
	    	  String CASCADE_FILE="haarcascade_frontalface_alt2.xml"; 
	    	  int SCALE=1;
			  String OUT_FILE = "markedFaces.jpg";
			  
	    	  /*for(int j=0;j<fileNames.size();j++){
	     		String imageFileName=fileNames.get(j);
	     		//System.out.print(imageFileName);
    	  }*/
     		IplImage origImg = cvLoadImage(imageFileName);

     		// convert to grayscale
			IplImage grayImg = IplImage.create(origImg.width(),origImg.height(), IPL_DEPTH_8U, 1);
			cvCvtColor(origImg, grayImg, CV_BGR2GRAY);
			
			// scale the grayscale (to speed up face detection)
			IplImage smallImg = IplImage.create(grayImg.width()/SCALE,grayImg.height()/SCALE, IPL_DEPTH_8U, 1);
			cvResize(grayImg, smallImg, CV_INTER_LINEAR);

			// equalize the small grayscale
			IplImage equImg = IplImage.create(smallImg.width(),smallImg.height(), IPL_DEPTH_8U, 1);
			cvEqualizeHist(smallImg, equImg);

			// create temp storage, used during object detection
			CvMemStorage storage = CvMemStorage.create();

			// instantiate a classifier cascade for face detection

			CvHaarClassifierCascade cascade =new CvHaarClassifierCascade(cvLoad(CASCADE_FILE));
        //      System.out.println("Detecting faces...");
			
			int confidence=0;
			CvSeq faces = cvHaarDetectObjects(equImg, cascade, storage,1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
			
			//making it less rigorous
			if(faces.total()==0)
			{
				cvClearMemStorage(storage);
				faces = cvHaarDetectObjects(equImg, cascade, storage,1.1, 2, CV_HAAR_DO_CANNY_PRUNING);
				confidence=3;
			}
			else
				confidence=5;
			
			if(faces.total()==0){
				cvClearMemStorage(storage);
				//faces = cvHaarDetectObjects(equImg, cascade, storage,1.1, 1, CV_HAAR_DO_CANNY_PRUNING);
				confidence=0;
			}
			
			cvClearMemStorage(storage);

			// draw thick yellow rectangles around all the faces
			int total = faces.total();
			String outRect ="";
         //    System.out.println("Found " + total + " face(s)");
			for (int i = 0; i < total; i++) {
			        CvRect r = new CvRect(cvGetSeqElem(faces, i));
			        cvRectangle(origImg, cvPoint( r.x()*SCALE, r.y()*SCALE ),cvPoint( (r.x() + r.width())*SCALE,(r.y() + r.height())*SCALE ),CvScalar.RED, 6, CV_AA, 0);
			        String strRect = String.format("CvRect(%d,%d,%d,%d)", r.x(), r.y(), r.width(), r.height());                       
			        outRect = outRect+ String.format("%d %d %d %d ", r.x(),r.y(),r.width(),r.height());
			        System.out.println(imageFileName);
			        System.out.println(strRect);
			        //undo image scaling when calculating rect coordinates
			}
			
			if (total > 0 && confidence==5) {
					OUT_FILE=imageFileName+"_out.jpg";
				/*	if(readFromFolder){
				 * try {
						out.write(imageFileName + " " + Integer.toString(total) + " " + outRect +"\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}*/
			        System.out.println("Saving marked-faces version of " + " in " + OUT_FILE);
			        System.out.println("confidence" + confidence);
			       cvSaveImage(OUT_FILE, origImg);
			        
				}
			return confidence;
		
		//}//for loop of all images
	/*	if(readFromFolder) {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		}*/
	}
    public static void main(String[] args) {

        FrontEnd chooseFile = new FrontEnd();
        System.out.print("hellooo");
        detect = new JButton ("Detect face");
        detect.addActionListener(new ActionListener() {
        
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                if(FaceDetect.selectedFileName!="")
                {
                	int confidence = FaceDetect.faceDetect(FaceDetect.selectedFileName);
                	String outputFileName=FaceDetect.selectedFileName+"_out.jpg";
                	if(resultPicLabel!=null){
              	    FaceDetect.panelTwo.remove(resultPicLabel);
              	    FaceDetect.panelTwo.repaint();
            	    FaceDetect.panelTwo.validate();
            	    resultPicLabel=null;
              		}
                	if(resultFaceConfidence!=null) {
                		FaceDetect.panelTwo.remove(resultFaceConfidence);
                		FaceDetect.panelTwo.repaint();
                	    FaceDetect.panelTwo.validate();
                	    resultFaceConfidence=null;
                	}
                	if(confidence !=0) {
                		resultPicLabel= new JLabel (FaceDetect.createImageLabel(outputFileName));
                		if(confidence==3) {
                			resultFaceConfidence=new JLabel("Not exactly a headshot but close enough!");
                		}
                	}
                	else {
                		resultFaceConfidence=new JLabel("This doesn't seem like a headshot, would you want to upload another one?");
                	}
                	//resultFaceConfidence.paintImmediately(resultFaceConfidence.getVisibleRect());
                	if(resultPicLabel!= null)
          	      		FaceDetect.panelTwo.add( resultPicLabel);
          	      	if(resultFaceConfidence !=null)
          	      		FaceDetect.panelTwo.add(resultFaceConfidence);
          	      	FaceDetect.panelTwo.validate(); 
          	      	FaceDetect.panelTwo.repaint();
                }
            }
        });      
 
        panelOne = new JPanel();
        panelTwo = new JPanel();
        
        panelOne.add(chooseFile);
        panelTwo.add(detect);
  
  
      //Create and set up the window.
        frame = new JFrame("HeadShot Detector");
        frame.setBounds(10,10,1000,1000);
        frame.getContentPane(); 
        frame.setLayout(new GridLayout(2, 2));
        frame.setPreferredSize(new Dimension(1000,1000));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addWindowListener(
          new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
              System.exit(0);
              }
            }
          );
        frame.add(panelOne);
        frame.add(panelTwo);
        frame.setVisible(true);
              
        //Display the window.
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        
   
}//main method
}