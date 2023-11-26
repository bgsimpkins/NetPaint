package client;
/*
 * PaintPanel.java
 *
 * Created on June 29, 2008, 11:39 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import audio.SimpleAudioPlayer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.*;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import message.AlertMessage;
import message.ChatMessage;
import message.ClearScreenMessage;
import message.FillMessage;
import message.MouseDragMessage;
import message.MouseMoveMessage;
import message.MouseReleaseMessage;
import message.PaintMessage;
/**
 *
 * @author Ben
 */
public class PaintPanel extends JPanel implements MouseListener,MouseMotionListener,ActionListener,Runnable
{
    public static final int DRAW_MODE = 1;
    public static final int FILL_MODE = 2;
    public static final int DROPPER_MODE = 3;

    private int mode = DRAW_MODE;

    private Point mousePoint = null;
    private Point lastMousePoint = null;
    
    //private Point extMousePoint = null;
    private Point extLastMousePoint = null;
    //private Point extMousePointIn = null;
    private boolean extMouseRelease = false;

    private boolean button1 = false;
    //private boolean dataIn = false;
    private boolean clearScreen = true;
   
    //private ServerListenerManager slm;
    Thread thread;
    
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;

    private ArrayList<MouseDragMessage> extDrawQ = new ArrayList();
    private ArrayList<Point> drawQ = new ArrayList();
    private ArrayList<FillMessage> fillQ = new ArrayList();
    private Color lineColor = Color.BLACK;
    private BasicStroke lineStroke = new BasicStroke(1);

    private Color extLineColor = Color.BLACK;
    private int extLineThickness  = 1;

    private PopupWindow popup;
    private ArrayList<Line2D> lines = new ArrayList();
    private ArrayList<Line2D> extLines = new ArrayList();
    private ArrayList<PaintPath> paths = new ArrayList();

    private ColorButton colorButton;

    private Point extMousePoint = new Point();

    private BufferedImage dbImage = null;

    private Point fillPoint = null;
    private boolean mouseRelease = false;

    private PaintClient pc;

    private SimpleAudioPlayer aPlayer;

    /**
     * Creates a new instance of PaintPanel
     */
    public PaintPanel(ObjectInputStream in, ObjectOutputStream out,PaintClient pc) {
        super();

        this.pc = pc;
        objectIn = in;
        objectOut = out;
        
        
        addMouseListener(this);
        addMouseMotionListener(this);
        
        setLayout(null);

        popup = new PopupWindow(this);


        thread = new Thread(this);
        thread.start();

        clearScreen = true;
        repaint();

        //slm = new ServerListenerManager(this,in);
        //slm.start();
    }

    private void initAudio()
    {
        aPlayer = new SimpleAudioPlayer(null);

    }

    public void saveImage()
    {
        try {
            ImageIO.write((BufferedImage) dbImage, "png", new File("savedImage.png"));
        } catch (IOException ex) {
            System.err.println("PaintPanel.saveImage(): Problem writing image!");
        }
    }

    public void setColorButton(ColorButton colorButton)
    {
        this.colorButton = colorButton;
    }

    
    @Override
    public void paintComponent(Graphics g){
        //Graphics2D g2 = (Graphics2D)g;

        if (dbImage == null)
        {
            dbImage = (BufferedImage)this.createImage(getSize().width, getSize().height);
        }

        Graphics2D g2 = (Graphics2D)dbImage.getGraphics();
        if (clearScreen){
            g2.setColor(Color.WHITE);
            g2.fillRect(0,0,getSize().width,getSize().height);
            clearScreen = false;
        }
        
        g2.setColor(lineColor);
        g2.setStroke(lineStroke);
        /*if (button1){
            if (lastMousePoint != null){
                g2.drawLine(lastMousePoint.x,lastMousePoint.y,mousePoint.x,mousePoint.y);
            }
                
        }*/

        
        
        if (!drawQ.isEmpty())
        {
            for (int i = 0; i < drawQ.size();i++)
            {
                Point p  = drawQ.get(i);
                if (lastMousePoint != null)
                {
                    g2.drawLine(lastMousePoint.x,lastMousePoint.y,p.x,p.y);
                    lastMousePoint.setLocation(p);

                }
                else
                {
                    lastMousePoint = new Point(p);
                }

            }
            drawQ.clear();
        }

        //If ext mouse released, process
        if (mouseRelease) {


            lastMousePoint = null;
            mouseRelease = false;
        }

        /*if (dataIn){
            if (extMousePoint != null){
                extLastMousePoint = new Point(extMousePoint.x,extMousePoint.y);    
                extMousePoint.setLocation(extMousePointIn);
                g2.drawLine(extLastMousePoint.x,extLastMousePoint.y,extMousePoint.x,extMousePoint.y);
            }

        }*/


        //Draw paths in history
        /*for (int i = 0; i < paths.size(); i++)
        {
            PaintPath p = paths.get(i);
            g2.setColor(p.getColor());
            g2.setStroke(p.getStroke());

            for (int j = 0; j < p.getLines().size(); j++)
            {
                Line2D l = p.getLines().get(j);
                g2.draw(l);
            }
        }*/

        //g2.setStroke(lineStroke);
        //g2.setColor(lineColor);

        //Draw path currently being drawn
        /*for (int i = 0; i < lines.size(); i++)
        {
            Line2D l = lines.get(i);
            g2.draw(l);
        }*/


        //If external data have come in, process them
        if (!extDrawQ.isEmpty())
        {
            for (int i = 0; i < extDrawQ.size();i++)
            {
                MouseDragMessage mm = extDrawQ.get(i);
                Point p = mm.getPoint();
                extLineColor = mm.getColor();
                extLineThickness = mm.getThickness();
                if (extLastMousePoint != null)
                {
                    g2.setColor(extLineColor);
                    g2.setStroke(new BasicStroke(extLineThickness));
                    g2.drawLine(extLastMousePoint.x,extLastMousePoint.y,p.x,p.y);
                    //extLines.add(new Line2D.Float(extLastMousePoint,p));
                    extLastMousePoint.setLocation(p);
                    
                }
                else
                {
                    extLastMousePoint = new Point(p);
                }
                
            }
            extDrawQ.clear();
        }

        //Draw external path being drawn
        /*g2.setColor(extLineColor);
        g2.setStroke(new BasicStroke(extLineThickness));
        for (int i = 0; i < extLines.size(); i++)
        {
            Line2D l = extLines.get(i);
            g2.draw(l);
        }*/

        //If ext mouse released, process
        if (extMouseRelease) {
            //paths.add(new PaintPath(extLines,extLineColor,extLineThickness));
            //extLines.clear();

            extLastMousePoint = null;
            extMouseRelease = false;
        }

        
        if(fillPoint != null)
        {
            g2.setColor(lineColor);
            g2.setStroke(new BasicStroke(1));

            g2.drawLine(fillPoint.x, fillPoint.y, fillPoint.x, fillPoint.y);
            fillPoint = null;
        }
        
        if (!fillQ.isEmpty())
        {
            for (int i = 0; i < fillQ.size(); i++)
            {
                FillMessage fm = fillQ.get(i);
                doFill(fm.getPoint(),fm.getColor());
               
            }
            fillQ.clear();
        }

        //Render the buffered image
        g.drawImage(dbImage,0,0,null);

        //Draw external mouse location
        g.setColor(Color.BLUE);
        g.drawLine(extMousePoint.x, extMousePoint.y-10, extMousePoint.x, extMousePoint.y-3);
        g.drawLine(extMousePoint.x, extMousePoint.y+3, extMousePoint.x, extMousePoint.y+10);
        g.drawLine(extMousePoint.x-10, extMousePoint.y, extMousePoint.x-3, extMousePoint.y);
        g.drawLine(extMousePoint.x+3, extMousePoint.y, extMousePoint.x+10, extMousePoint.y);
    }


    /**Performs flood fill using low memory LIFO queue algorithm described here:
     * <a href="http://en.wikipedia.org/wiki/Flood_fill">Flood Fill</a>
     * @param p
     */
    private void doFill(Point p,Color fillColor)
    {
        WritableRaster wr = dbImage.getRaster();

        float[] repCol = new float[]{fillColor.getRed(),fillColor.getGreen(),fillColor.getBlue(),255};
        
        float[] aCol = new float[4];
        
        ///System.out.println("Rep color="+repCol.toString());
        float[] targetCol = null;
        targetCol = wr.getPixel(p.x, p.y, targetCol);
        if (sameColors(repCol,targetCol)) return;
        ArrayList<Point> q = new ArrayList();
        q.add(p);

        float[] nCol = new float[4];

//        while(!q.isEmpty())
//        {
//            //wr  = dbImage.getRaster();
//            Point n = q.remove(q.size()-1);
//            wr.getPixel(n.x,n.y,nCol);
//            //System.out.println("Processing: "+n.x+" "+n.y);
//            //System.out.println("targetCol="+targetCol[0]+" "+targetCol[1]+" "+targetCol[2]);
//            //System.out.println("nCol="+nCol[0]+" "+nCol[1]+" "+nCol[2]);
//
//            //System.out.println("Same colors="+sameColors(nCol,targetCol));
//
//            if (sameColors(nCol,targetCol))
//            {
//                //dbImage.setRGB(n.x, n.y, repCol.getRGB());
//                //fillPoint = n;
//                wr.setPixel(n.x, n.y,repCol);
//                //dbImage.setData(wr);
//
//                Point west = new Point(n.x-1,n.y);
//                if (west.x >= 0) q.add(west);
//                Point east = new Point(n.x+1,n.y);
//                if (east.x < dbImage.getWidth()) q.add(east);
//                Point north = new Point(n.x,n.y-1);
//                if (north.y >= 0) q.add(north);
//                Point south = new Point(n.x,n.y+1);
//                if (south.y < dbImage.getHeight()) q.add(south);
//                //repaint();
//
//            }
//
//        }
        
        //This method decreases queue overhead by filling in lines as it goes (see second algorithm on wikipedia page
        
        while(!q.isEmpty())
        {
            Point n = q.remove(q.size()-1);
            wr.getPixel(n.x,n.y,nCol);
            
            if (sameColors(nCol,targetCol))
            {
                int w = n.x;
                int e = n.x;
                while(true)
                {
                    w--;
                    if (w < 0) break;
                    wr.getPixel(w,n.y,aCol);
                    if (!sameColors(aCol,targetCol)) break;

                    if (n.y-1 >= 0 && sameColors(aCol,targetCol))
                    {
                        Point north = new Point(w,n.y-1);
                        q.add(north);
                    }
                    if (n.y+1 < dbImage.getHeight() && sameColors(aCol,targetCol))
                    {
                        Point south = new Point(w,n.y+1);
                        q.add(south);
                    }
                    
                    //Set node to replacement node color
                    wr.setPixel(w,n.y,repCol);
                    
                }
                while(true)
                {
                    e++;
                    if (e >= dbImage.getWidth()) break;
                    wr.getPixel(e,n.y,aCol);
                    if (!sameColors(aCol,targetCol)) break;
                    
                    if (n.y-1 >= 0 && sameColors(aCol,targetCol))
                    {
                        Point north = new Point(e,n.y-1);
                        q.add(north);
                    }
                    if (n.y+1 < dbImage.getHeight() && sameColors(aCol,targetCol))
                    {
                        Point south = new Point(e,n.y+1);
                        q.add(south);
                    }
                    //Set node to replacement node color
                    wr.setPixel(e,n.y,repCol);
                }
                //Set node to replacement node color
                wr.setPixel(n.x,n.y,repCol);
            }
        }
        
        
        dbImage.setData(wr);
        repaint();

        
    }

    private void getColorOfPixel(Point p)
    {
        float[] colData = null;
        colData = dbImage.getData().getPixel(p.x, p.y, colData);
        System.out.println("New color="+colData[0]+" "+ colData[1]+ " "+colData[2]);
        Color color = new Color((int)colData[0],(int)colData[1],(int)colData[2]);
        lineColor = color;
        colorButton.setColor(color);
        colorButton.repaint();
    }

    private boolean sameColors(float[] c1, float[] c2)
    {
        return c1[0]==c2[0] &&
                c1[1]==c2[1] &&
                c1[2]==c2[2];
    }
        
    public void run(){
        while(true){
            try {
                
                Object message = objectIn.readObject();

                if (message instanceof MouseDragMessage) {
                    MouseDragMessage md = (MouseDragMessage)message;
                    extDrawQ.add(md);
                    extMousePoint.setLocation(md.getPoint());
                }else if (message instanceof MouseReleaseMessage) extMouseRelease = true;
                else if (message instanceof MouseMoveMessage)
                {
                    MouseMoveMessage mm = (MouseMoveMessage)message;
                    extMousePoint.setLocation(mm.getPoint());
                }else if (message instanceof ClearScreenMessage)
                {
                    int ans = JOptionPane.showConfirmDialog(null, "The other user has cleared his/her screen. Clear yours?");

                    if (ans == JOptionPane.OK_OPTION)
                    {
                        clearScreen = true;
                        repaint();
                    }
                }else if (message instanceof AlertMessage) {
                    AlertMessage am = (AlertMessage)message;
                    if (am.getId().equals("Capacity Reached")) setVisible(false);
                                      
                    JOptionPane.showMessageDialog(null, am.getMessage());
                    if (am.getId().equals("Capacity Reached")) System.exit(1);
                    
                }else if (message instanceof FillMessage) {
                    FillMessage fm = (FillMessage)message;
                    //doFill(fm.getPoint(),fm.getColor());
                    fillQ.add(fm);
                }else if (message instanceof ChatMessage)
                {
                    ChatMessage cm = (ChatMessage)message;
                    System.out.println("Received message: "+cm.getMessage());
                    pc.receiveMessage((ChatMessage)message);
                }
                
            } catch (IOException ex) {}
            catch (ClassNotFoundException ex){}
            repaint();    
        }
    }
    
    public void actionPerformed(ActionEvent ae){
        if (ae.getSource() instanceof JComboBox)
        {
            JComboBox cb = (JComboBox)ae.getSource();
            String sel = (String)cb.getSelectedItem();
            lineStroke = new BasicStroke(Integer.parseInt(sel));
        }
        else if (ae.getActionCommand().equals("OK"))
        {
            lineColor = popup.getColor();
            colorButton.setColor(lineColor);
            popup.setVisible(false);
        }
        
        else if(ae.getActionCommand().equals("Clear"))
        {
            int option = JOptionPane.showConfirmDialog(null, "Do you really want to clear the image?");
            if (option == JOptionPane.YES_OPTION) 
            {
                //paths.clear();
                clearScreen = true;
                repaint();
                sendMessage(new ClearScreenMessage());
            }
            
            //getParent().repaint();
        }else if (ae.getActionCommand().equals("Color"))
        {
            popup.setVisible(true);
        }
        else if (ae.getActionCommand().equals("Export Image"))
        {
            System.out.println("Writing image!");
            saveImage();
        }
        else if (ae.getActionCommand().equals("Draw"))
        {
            mode = DRAW_MODE;
            pc.setMode(mode);
        }
        else if (ae.getActionCommand().equals("Fill"))
        {
            mode = FILL_MODE;
            pc.setMode(mode);
        }else if (ae.getActionCommand().equals("Dropper"))
        {
            System.out.println("Dropper!");
            mode = DROPPER_MODE;
            pc.setMode(mode);
        }


    }
    public void mouseEntered(MouseEvent me){
        //dataIn = false;
    }
    public void mouseExited(MouseEvent me){
        //dataIn = false;
    }
    public void mousePressed(MouseEvent me){

        button1 = true;
        //lastMousePoint = me.getPoint();

        //if (mode==DRAW_MODE) sendMessage(new MouseDragMessage(mousePoint,lineColor,(int)lineStroke.getLineWidth()));
        if (mode==FILL_MODE) {
            //doFill(me.getPoint(),lineColor);
            FillMessage fm = new FillMessage(me.getPoint(),lineColor);         
            sendMessage(fm);
            fillQ.add(fm);
        }
        else if (mode== DROPPER_MODE)
        {
            getColorOfPixel(me.getPoint());
        }
    }
    public void mouseReleased(MouseEvent me){
        this.button1 = false;

        //paths.add(new PaintPath(lines,lineColor,(int)lineStroke.getLineWidth()));
        //lines.clear();
        mousePoint = me.getPoint();
        drawQ.add(mousePoint);

        mouseRelease = true;

        if (mode==DRAW_MODE)
        {
            sendMessage(new MouseDragMessage(mousePoint,lineColor,(int)lineStroke.getLineWidth()));
            sendMessage(new MouseReleaseMessage(me.getPoint()));
        }
        
        repaint();
    }
    public void mouseClicked(MouseEvent me){}
    public void mouseMoved(MouseEvent me){
        mousePoint = me.getPoint();
        //dataIn = false;

        sendMessage(new MouseMoveMessage(mousePoint));
    }
    public void mouseDragged(MouseEvent me){
        //lastMousePoint = mousePoint;
        mousePoint = me.getPoint();
        drawQ.add(mousePoint);

        /*if (lastMousePoint != null)
        {
            Line2D line = new Line2D.Float(lastMousePoint,mousePoint);
            lines.add(line);
        }*/

        if (mode == DRAW_MODE) sendMessage(new MouseDragMessage(mousePoint,lineColor,(int)lineStroke.getLineWidth()));
        repaint();
        
    }
    
    public void sendMessage(PaintMessage m)
    {
        try {
            objectOut.writeObject(m);
            objectOut.reset();
        } catch (IOException ex) {
            System.out.println("IO Exception in writing PaintMessage to server: "+m.getClass());
            ex.printStackTrace();
        }
    }

    /**
     * @return the mode
     */
    public int getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    class PaintPath
    {
        Color color;
        ArrayList<Line2D> lines = new ArrayList();
        BasicStroke stroke;
        public PaintPath(ArrayList<Line2D> lines,Color color,int thick)
        {
            this.lines.addAll(lines);
            this.color = color;
            stroke = new BasicStroke(thick);
        }

        public ArrayList<Line2D> getLines()
        {
            return lines;
        }

        public Color getColor()
        {
            return color;
        }

        public BasicStroke getStroke()
        {
            return stroke;
        }
    }

    class FillThread extends Thread
    {
        public Point p;

        @Override
        public void run()
        {
            Raster r  = dbImage.getData();

            float[] targetCol = new float[4];
            Color repCol = lineColor;
            System.out.println("Rep color="+repCol.toString());
            r.getPixel(p.x, p.y, targetCol);
            ArrayList<Point> q = new ArrayList();
            q.add(p);

            float[] nCol = new float[4];

            while(!q.isEmpty())
            {
                r  = dbImage.getData();
                Point n = q.remove(q.size()-1);
                r.getPixel(n.x,n.y,nCol);
                System.out.println("Processing: "+n.x+" "+n.y);
                System.out.println("targetCol="+targetCol[0]+" "+targetCol[1]+" "+targetCol[2]);
                System.out.println("nCol="+nCol[0]+" "+nCol[1]+" "+nCol[2]);

                System.out.println("Same colors="+sameColors(nCol,targetCol));

                if (sameColors(nCol,targetCol))
                {


                    dbImage.setRGB(n.x, n.y, repCol.getRGB());

                    //fillPoint = n;


                    Point west = new Point(n.x-1,n.y);
                    if (west.x >= 0) {
                        q.add(west);
                        System.out.println("Adding west");
                    }
                    Point east = new Point(n.x+1,n.y);
                    if (east.x < dbImage.getWidth()) {
                        q.add(east);

                        System.out.println("Adding east");
                    }
                    Point north = new Point(n.x,n.y-1);
                    if (north.y >= 0) {
                        q.add(north);
                        System.out.println("Adding north");
                    }
                    Point south = new Point(n.x,n.y+1);
                    if (south.y < dbImage.getHeight()) {
                        q.add(south);
                        System.out.println("Adding south");
                    }
                    repaint();

                }

            }
        }
    }
}
