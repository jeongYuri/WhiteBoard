package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Handler extends Thread{
    private Socket s;
    private BufferedReader i;
    private PrintWriter o;
    private WhiteboardServer server;

    public Handler(WhiteboardServer server, Socket s)throws IOException{
        this.s = s;
        this.server = server;
        i = new BufferedReader(new InputStreamReader(s.getInputStream()));
        o = new PrintWriter(s.getOutputStream(),true);
    }
    public void run(){
        try {
            server.register(this);
            while(true){
                broadcast(i.readLine());
            }
        }catch (IOException ex){
            //PrintDebugMessage.print(ex);
        }server.unregister(this);
        try{
            i.close();
            o.close();
            s.close();
        }catch(IOException ex){
            //PrintDebugMessage.print(ex);
        }
    }
    protected  void println(String msg){
        o.println(msg);
    }
    protected void broadcast(String msg){
        server.broadcast(msg);
    }
}
