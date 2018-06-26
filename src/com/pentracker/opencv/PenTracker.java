package com.pentracker.opencv;

import java.util.ArrayList;
import java.util.LinkedList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;

public class PenTracker
{
	protected ImageProcessor imagePs = new ImageProcessor();
	protected ArrayList <HL_Object> hlObjs = new ArrayList <HL_Object>();
	
	protected LinkedList <MatOfPoint> contours = new LinkedList <MatOfPoint>();
	
	protected double ratio = 1/2;
	
	public PenTracker(ImageProcessor imagePs)
	{
		this.imagePs = imagePs;
	}
	
	public void process()
	{
		GetContour();
		objectSet();
	}
	
	protected void GetContour()
	{
		Mat pSH_Mask = imagePs.pSH_Mask;
		
		//¸ð¼­¸®
		Mat white = new Mat (pSH_Mask.size(), pSH_Mask.type());
		contours.clear();
		Imgproc.findContours(pSH_Mask, contours, white, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
	}
	
	protected void objectSet()
	{
		this.hlObjs.clear();
		
		for(MatOfPoint thecontour : contours)
		{
			double theratio = calRatio(thecontour);
			if(theratio >= ratio)
			{
				hlObjs.add(new HL_Object(thecontour, imagePs.pSH_Mask.size()));
			}
			else
			{
				System.out.println(this + " " + theratio);
			}
			
		}
	}
	
	protected double calRatio(MatOfPoint thecontour)
	{
		MatOfPoint2f  thecontour2f = new MatOfPoint2f(thecontour.toArray() );
		RotatedRect sqar = Imgproc.minAreaRect(thecontour2f);
		Point pt[] = new Point[4];
		sqar.points(pt);
		
		double len0, len1 ,res;
		len0 = Math.sqrt(Math.pow(pt[0].x - pt[1].x, 2) + Math.pow(pt[0].y - pt[1].y, 2));
		len1 = Math.sqrt(Math.pow(pt[0].x - pt[3].x, 2) + Math.pow(pt[0].y - pt[3].y, 2));
		
		res =  len0 / len1;
		
		if (res > 1)
		{
			res = 1 / res;
		}
		
		return res;
	}

	public LinkedList<MatOfPoint> getContours()
	{
		return contours;
	}

	public void setContours(LinkedList<MatOfPoint> contours)
	{
		this.contours = contours;
	}

	public ArrayList<HL_Object> getHlObjs()
	{
		return hlObjs;
	}

	public void setHlObjs(ArrayList<HL_Object> hlObjs)
	{
		this.hlObjs = hlObjs;
	}
	
	
}
