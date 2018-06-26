package com.pentracker.UI;

import java.util.ArrayList;
import java.util.LinkedList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import com.pentracker.opencv.HL_Object;
import com.pentracker.opencv.ImageProcessor;
import com.pentracker.opencv.PenTracker;

public class Main
{
	protected static final String OPENCV_DLL_DIR = "C:\\opencv\\build\\java\\x64\\opencv_java341.dll";

	protected static ImageProcessor imgPS;
	protected static PenTracker penTrack;
    
    // Matrix for storing image
	protected static LinkedList <Mat> outimages = new LinkedList <Mat>();
    
    // Frame for displaying image
	protected static LinkedList <MyFrame> outframes = new LinkedList <MyFrame>();
	
	public static void main(String[] args)
	{
		loadLibs(args);
		
		VideoCapture cap = webcamload(1);
		Mat camimg = new Mat();
		
		imgPS = new ImageProcessor();
		penTrack = new PenTracker(imgPS);
		
		outimages.add(camimg);					//0
		outimages.add(imgPS.getSH_Mask());		//1
		//outimages.add(imgPS.getMOVE_Mask());	//2
		
		outFrameinit();
		
        // Main loop
        while (true)
        {
        	// Read current camera frame into matrix
            cap.read(camimg);
            
            imgPS.process(camimg);
            penTrack.process();
            
        	outFrame();
        }
	}
	
	protected static void outFrame()
	{
        // Render frame if the camera is still acquiring images
		for (int i = 0; i < outimages.size(); i++)
		{
			Mat theimage = outimages.get(i);
			MyFrame theframe = outframes.get(i);
			
	        if (!theimage.empty()) 
	        {
	            drawSome(theimage, i);
	            
	        	theframe.render(theimage);
	        } 
	        else 
	        {
	            System.out.println("<" + theimage + "> " + "No captured frame " + i);
	        }
		}
	}
	
	protected static void outFrameinit()
	{
		for (int i = 0; i < outimages.size(); i++)
		{
			outframes.add(new MyFrame());
		}
		
		
		for(MyFrame theframe : outframes)
		{
			theframe.setVisible(true);
		}
	}
	
	protected static VideoCapture webcamload(int camnum)
	{
		// Register the default camera
        VideoCapture cap = new VideoCapture(camnum);
 
        // Check if video capturing is enabled
        if (!cap.isOpened())
        {
        	System.out.println("<" + cap + "> " + "Can not connect camera " + camnum);
            System.exit(-1);
        }
        
		return cap;
	}
	
	protected static void drawSome(Mat img, int imgnum)
	{
		switch(imgnum)
		{
			case 0:
				//Size imgsize = img.size();
				//final double FONT_SCALE = 1;
				//final int FONT_FACE = 1;
				final Scalar Scalar_RED = new Scalar(0, 0, 255);
				final Scalar Scalar_GRN = new Scalar(0, 255, 0);
				final Scalar Scalar_YEL = new Scalar(0, 255, 255);
				final Scalar Scalar_BLU = new Scalar(255, 0, 0);/*
				String str = "";
				Mat convimg = imgPS.getImage();
				
				double[] out_px = convimg.get((int)imgsize.width / 2, (int)imgsize.height / 2);
				
				int out_H = (int)out_px[2];
				int out_S = (int)out_px[1];
				int out_V = (int)out_px[0];
				
				Imgproc.circle(img, new Point(imgsize.width / 2, imgsize.height / 2), 3, Scalar_RED);
				str = out_H + "," + out_S + "," + out_V;
				
				out_px = outimages.get(0).get((int)imgsize.width / 2, (int)imgsize.height / 2);
				
				int out_R = (int)out_px[2];
				int out_G = (int)out_px[1];
				int out_B = (int)out_px[0];
				
				Imgproc.putText(img, str, new Point(imgsize.width / 2, imgsize.height / 2 + 15), FONT_FACE, FONT_SCALE, Scalar_RED);
				str = out_R + "," + out_G + "," + out_B;
				Imgproc.putText(img, str, new Point(imgsize.width / 2, imgsize.height / 2), FONT_FACE, FONT_SCALE, Scalar_RED);
				*/
				
				LinkedList <MatOfPoint> imgPsContours = penTrack.getContours();
				if(imgPsContours.size() > 0)
				{
					Imgproc.drawContours(img, imgPsContours, -1, Scalar_GRN, 1);
					try
					{
						ArrayList <HL_Object> trakObj = penTrack.getHlObjs();
						
						int dmaxArea = 0;
						int maxArea = 0;
						
						for(int i = 0; i < trakObj.size(); i ++)
						{
							int a = trakObj.get(i).getArea();
							if(a > dmaxArea)
							{
								dmaxArea = a;
								maxArea = i;
							}
						}
						Imgproc.circle(img, trakObj.get(maxArea).getCenter(), 5, Scalar_BLU, -1, Core.LINE_AA, 0);
						
						for(HL_Object theObj : trakObj)
						{
							Point point = theObj.getCenter();
							Imgproc.circle(img, point, (int) (Math.sqrt(theObj.getArea())), Scalar_YEL, 1, Core.LINE_AA, 0);
							Imgproc.circle(img, point, 2, Scalar_RED, 1, Core.LINE_AA, 0);
						}
						
					}
					catch(java.lang.NullPointerException e)
					{}
					catch(java.lang.IndexOutOfBoundsException e)
					{}
				}
				break;
		}
	}
	
	protected static void loadLibs(String dir[])
	{
		if(dir.length == 0)
		{
			System.load(OPENCV_DLL_DIR);
		}
		else
		{
			System.load(dir[0]);
		}
	}
}
