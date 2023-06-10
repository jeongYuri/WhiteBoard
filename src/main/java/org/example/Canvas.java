package org.example;

import javax.management.monitor.MonitorSettingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.StringTokenizer;
import java.util.Vector;

public class Canvas extends JPanel
implements Runnable, MouseListener, MouseMotionListener {
    public static final int None =0;
    public static final int LINE =1;
    public static final int CIRCLE =2;
    public static final int RECT = 3;
    public static final int Save = 4;

    private PrintWriter o;
    private BufferedReader i;
    private int mode;
    public Vector pictures;
    private int tempX, tempY;
    private Thread listener;
    String a;

    public Canvas(String host, int port) {
        pictures = new Vector();
        addMouseListener(this);
        addMouseMotionListener(this);
        try {
            Socket s = new Socket(host, port);
            o = new PrintWriter(s.getOutputStream(), true);
            i = new BufferedReader(new InputStreamReader(s.getInputStream()));
            listener = new Thread(this);
            listener.start();
        } catch (Exception e) {
            //PrintDebugMessage.print(e);
        }
    }
    public void paint(Graphics g){
        super.paint(g);

        int n= pictures.size();
        for(int i=0; i<n; i++){
            Drawable d = (Drawable) pictures.elementAt(i);
            d.paint(g);
            a = pictures.toString();
        }
    }
    public void run(){
        try{
            while(true){
                String line = i.readLine();
                if(line.equals("!x")){
                    clear();
                    repaint();
                }else if(line !=null){
                    draw(line);
                }
            }
        }catch (Exception e){
            //PrintDebugMessage.print(e);
        }
    }
    public void send(String msg){
        o.println(msg);
    }
    public void open(){
        File file  = new File("C:\\Users\\dydrk\\a.txt");
        pictures.removeAllElements();
        try{
            FileReader reader = new FileReader(file);
            BufferedReader buf = new BufferedReader(reader);
            String line ="";
            while((line=buf.readLine())!=null){
                send(line);
            }
            buf.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void clear(){
        pictures.removeAllElements();
        repaint();
    }
    public void draw(String data){
        //PrintDebugMessage.print(data);
        int[] d = new int[5];
        StringTokenizer  st = new StringTokenizer(data,":",false);
        int index=0;
        while(st.hasMoreTokens()){
            d[index]=Integer.parseInt(st.nextToken());
            index ++;
        }switch (d[0]){
            case None :break;
            case LINE:
                pictures.addElement(new Line(d[1], d[2], d[3], d[4]));
                break;
            case CIRCLE :
                pictures.addElement(new Circle(d[1], d[2], d[3], d[4]));
                 break;
            case RECT :
                pictures.addElement(new Rect(d[1], d[2], d[3], d[4]));
                break;
        }
        repaint();
    }
    public void setMode(int m){
        mode = m;
    }
    public void mouseClicked(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}

    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        String msg;
        switch(mode) {
            default:
                return;
            case LINE:
                tempX = x;
                tempY = y;
                break;
            case CIRCLE:
                msg = CIRCLE + ":" + x + ":" + y + ":" + "10" + ":" + "10";
                send(msg);
                break;
            case RECT:
                msg = RECT + ":" + x + ":" + y + ":" + "10" + ":" + "10";
                send(msg);
                break;
        }
    }
    public void mouseMoved(MouseEvent e){}
    public void mouseDragged(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        String msg;
        switch(mode) {
            case None: return;
            case LINE:
                msg = LINE+":"+tempX+":"+tempY+":"+x+":"+y;
                send(msg);
                tempX = x;
                tempY = y;
                break;
            default:
                return;
}

    }

}
