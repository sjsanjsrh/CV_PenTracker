/**
 * 
 */
package com.pentracker.opencv;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

/**
 * @author jhpark
 *
 */
public class HL_Object
{
	//protected MatOfPoint contour;
	protected Moments moment;
	protected Point center;
	protected int area;
	protected Size pageSize;
	
	public HL_Object(MatOfPoint contour, Size pageSize)
	{
		moment = Imgproc.moments(contour);
		center = new Point(moment.m10 / (moment.m00 + 1e-5), moment.m01 / (moment.m00 + 1e-5));
		this.pageSize = pageSize;
		area = (int) Imgproc.contourArea(contour);
	}

	public Moments getMoment()
	{
		return moment;
	}

	public void setMoment(Moments moment)
	{
		this.moment = moment;
	}

	public Point getCenter()
	{
		return center;
	}

	public void setCenter(Point center)
	{
		this.center = center;
	}

	public int getArea()
	{
		return area;
	}

	public void setArea(int area)
	{
		this.area = area;
	}
	
	
}
