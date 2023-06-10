package org.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class WhiteboardServer {
    private Vector handlers;
    public WhiteboardServer(int port){
        try{
            ServerSocket server = new ServerSocket(port);
            handlers = new Vector();
            System.out.println("ready");
            while(true){
                Socket s = server.accept();
                Handler c = new Handler(this,s);
                c.start();
            }
        }catch (Exception e){
            //PrintDebugMessage.print(e);
        }
    }
    public Object getHandler(int index){
        return handlers.elementAt(index);
    }
    public void register(Handler c){
        handlers.addElement(c);
    }
    public void unregister(Object o){
        handlers.removeElement(o);
    }
    public void broadcast(String msg){
        synchronized (handlers){
            int n = handlers.size();
            for(int i=0;i<n;i++){
                Handler c = (Handler)handlers.elementAt(i);
                try{
                    c.println(msg);
                }catch(Exception ex){
                    //PrintDebugMessage.print(ex);
                }
            }
        }
    }
    public static void main(String args[]){
        WhiteboardServer server = new WhiteboardServer(8080);
    }
}
