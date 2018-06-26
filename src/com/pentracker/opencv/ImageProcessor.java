package com.pentracker.opencv;

import java.util.LinkedList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageProcessor
{
	protected Mat image = new Mat();
	LinkedList <Mat> HSVimage = new LinkedList <Mat>();
	protected double saturationMax = 1;	//mask filter maximum
	protected double saturationMin = 0;	//mask filter minimum
	protected double HUEMax = 255;	//mask filter maximum
	protected double HUEMin = 254;	//mask filter minimum
	protected double noizeSize = 5;
	
	protected Mat SH_Mask = new Mat();
	protected Mat pSH_Mask = new Mat();
	//protected Mat MOVE_Mask = new Mat();
	
	public void process(Mat img)
	{
		this.image = img.clone();

		convertToHSV(this.image);
		
		maskCrate();
	}

	protected void maskCrate()
	{
		Mat saturationMask = new Mat();
		Mat HUEMask = new Mat();
		
		//휘도, 체도 필터
		Core.inRange(getImageS(), new Scalar(saturationMin), new Scalar(saturationMax), saturationMask);
		Core.inRange(getImageH(), new Scalar(HUEMin), new Scalar(HUEMax), HUEMask);
		
		//휘도, 체도 and연산
		Core.bitwise_and(saturationMask, HUEMask, SH_Mask);
		
		//denoize(SH_Mask, noizeSize);
		
		if(!pSH_Mask.empty())
		{
	
			//Core.bitwise_xor(SH_Mask, pSH_Mask,MOVE_Mask);
		}
		
		pSH_Mask = SH_Mask.clone();
		
		denoize(pSH_Mask, noizeSize);
		
		
	}
	
	
	protected void convertToHSV(Mat img)
	{
		Imgproc.cvtColor(img, this.image, Imgproc.COLOR_RGB2HSV);
		Core.split(this.image, HSVimage);
	}
	
	
	protected void denoize(Mat img, double size)
	{
		Size noizeSize = new Size(size, size);
		
		//morphological opening 작은 점들을 제거 
		Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, noizeSize));
		Imgproc.dilate(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, noizeSize));


		//morphological closing 영역의 구멍 메우기 
		Imgproc.dilate(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, noizeSize));
		Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, noizeSize));
	}
	
	
	
	
	//-------------get,set--------------------//
	public Mat getImage()
	{
		return image;
	}

	public Mat getImageH()
	{
		return HSVimage.get(2);
	}
	
	public Mat getImageS()
	{
		return HSVimage.get(1);
	}
	
	public Mat getImageV()
	{
		return HSVimage.get(0);
	}

	public void setImage(Mat image)
	{
		this.image = image;
	}

	public Mat getSH_Mask()
	{
		return SH_Mask;
	}

	public void setSH_Mask(Mat sandHMask)
	{
		SH_Mask = sandHMask;
	}
/*
	public Mat getMOVE_Mask()
	{
		return MOVE_Mask;
	}

	public void setMOVE_Mask(Mat mOVE_Mask)
	{
		MOVE_Mask = mOVE_Mask;
	}*/
}
