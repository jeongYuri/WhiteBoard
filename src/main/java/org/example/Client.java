package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class Client extends JFrame implements ActionListener, Printable {
    private Canvas canvas;
    private JButton line, oval, clear, rect, Open, Save, Print;
    private JTextArea output;
    private JTextField input;
    private JLabel label;
    private Thread listener;
    private String host;
    public static final int None =0;
    public static final int LINE = 1;
    public static final int CIRCLE =2;
    public static final int RECT =3;

    public Client(String host, int port) {
        super("whiteboard");
        JToolBar tools = new JToolBar();
        line = new JButton("line");
        line.addActionListener(this);
        oval = new JButton("circle");
        oval.addActionListener(this);
        rect = new JButton("rect");
        rect.addActionListener(this);
        clear = new JButton("clean");
        clear.addActionListener(this);
        Open = new JButton("open");
        Open.addActionListener(this);
        Save = new JButton("save");
        Save.addActionListener(this);
        Print = new JButton("print");
        Print.addActionListener(this);

        tools.add(line);
        tools.add(oval);
        tools.add(rect);
        tools.addSeparator();
        tools.add(clear);
        tools.add(Open);
        tools.add(Save);
        tools.add(Print);
        canvas = new Canvas(host, port);

        getContentPane().add("North", tools);
        getContentPane().add("Center", canvas);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(100, 400);
        setVisible(true);

        output = new JTextArea();
        output.setColumns(15);
        getContentPane().add(new JScrollPane(output), "East");
        output.setEditable(false);

        Panel bottom = new Panel(new BorderLayout());
        label = new JLabel("name");
        bottom.add(label, "West");
        input = new JTextField();
        input.addActionListener(this);
        bottom.add(input, "Center");

        getContentPane().add(bottom, "South");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        Object c = e.getSource();
        if (c == line) {
            canvas.setMode(Canvas.LINE);
        } else if (c == oval) {
            canvas.setMode(Canvas.CIRCLE);
        } else if (c == rect) {
            canvas.setMode(Canvas.RECT);
        } else if (c == clear) {
            canvas.send("!x");
            canvas.clear();
        } else if (c == Open) {
            canvas.open();
        } else if (c == Save) {
            File file  = new File("C:\\Users\\dydrk\\a.txt");
            try {
                FileWriter w = new FileWriter(file);
                for (int i = 0; i < canvas.pictures.size(); i++) {
                    w.write(canvas.pictures.elementAt(i).toString());
                    w.write("\n");
                }
                w.close();
                System.out.println(canvas.pictures.toString());
                System.out.println("save");
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        } else if (c == Print) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(Client.this);
            boolean ok = job.printDialog();
            if (ok) {
                try {
                    job.print();
                } catch (PrinterException ex) {
                }
            }
        }
    }

        public int print(Graphics g, PageFormat pf, int page) throws PrinterException{
        if(page>0){
            return NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(),pf.getImageableY());
        for(int i=0;i<canvas.pictures.size();i++) {
            String data = canvas.pictures.elementAt(i).toString();
            int[] d = new int[5];
            StringTokenizer st = new StringTokenizer(data, ":",false);
            int index = 0;
            while (st.hasMoreTokens()) {
                d[index] = Integer.parseInt(st.nextToken());
                index++;
            }
            switch (d[0]) {
                case None:break;
                case LINE: g.drawLine(d[1], d[2], d[3], d[4]);
                break;
                case CIRCLE: g.drawOval(d[1], d[2], d[3], d[4]);
                break;
                case RECT :g.drawRect(d[1], d[2], d[3], d[4]);
                break;
            }
        }return PAGE_EXISTS;
            }
            public static  void main (String[] args){
                 new Client("localhost",8080);
        }


    }

