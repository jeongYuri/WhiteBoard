package org.example;
import java.awt.Graphics;
import java.awt.*;

public class Circle implements Drawable{
    private int x,y,w,h;
    public Circle(int a,int b, int c, int d){
        x=a;
        y=b;
        w=c;
        h=d;
    }
    public void paint(Graphics g){
        g.drawOval(x,y,w,h);
    }
    public String toString(){
        return "2"+":"+x+":"+y+":"+w+":"+h;
    }
}
