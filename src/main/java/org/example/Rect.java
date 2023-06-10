package org.example;
import java.awt.Graphics;
public class Rect implements Drawable {
    private int x,y,w,h;
    public Rect(int a,int b, int c, int d){
        x=a;
        y=b;
        w=c;
        h=d;
    }

    public void paint(Graphics g) {
        g.drawRect(x,y,w,h);
    }
    public String toString(){
        return "3"+":"+x+":"+y+":"+w+":"+h;
    }
}
