package com.pentracker.opencv;

import java.util.LinkedList;

import org.opencv.core.Point;

public class Pen
{
    Point tip = new Point(Double.NaN, Double.NaN);
    LinkedList<Point> trace = new LinkedList<Point>();
    
    public Pen()
    {
    }
    
    public void drawPen(Point target)
    {
        if(!Double.isNaN(this.tip.x))
        {
            trace.add(tip);
        }
        
        this.tip = target;
    }
    
    public void clearPen()
    {
        this.trace.clear();
        this.tip = new Point(-1,-1);
    }
    
    public LinkedList<Point> getTrace()
    {
        LinkedList<Point> tmp = new LinkedList<Point>(trace);
        tmp.add(tip);
        
        return tmp;
    }
}
