package org.example;
import java.awt.Graphics;
public class Line implements Drawable{
    private int sx,sy,ex,ey;
    public Line(int a,int b,int c, int d){
        sx=a;
        sy=b;
        ex=c;
        ey=d;
    }
    public void paint(Graphics g){
        g.drawLine(sx,sy,ex,ey);
    }
    public String toString(){
        return "1"+":"+sx+":"+sy+":"+ex+":"+ey;
    }
}
