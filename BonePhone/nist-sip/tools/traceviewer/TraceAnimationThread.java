package traceviewer ;

import java.awt.Component;
import java.util.Vector;

public class TraceAnimationThread implements Runnable{

    Thread traceThread;
    TraceCanvas traceCanvas;
    Vector arrows;
    int currentIndex;
    int delay;


    public TraceAnimationThread(TraceCanvas tracecanvas){
        traceThread = null;
        traceCanvas = null;
        arrows = null;
        currentIndex = 0;
        delay = 2000;
        traceCanvas = tracecanvas;
        arrows = tracecanvas.getArrows();
    }

    public void start(){
        traceThread = new Thread(this);
        traceThread.setPriority(1);
        traceThread.start();
    }

    public void stop(){
        traceThread = null;
        currentIndex = 0;
    }

    public void run(){
        while(traceThread != null){
            
            
            if(currentIndex == arrows.size()){
                currentIndex = 0;
                traceCanvas.setAllArrowsVisible(false);
            }
            Arrow arrow = (Arrow)arrows.elementAt(currentIndex++);
            arrow.visible = true;
            traceCanvas.repaint();
            traceCanvas.selectMessage(arrow.xmin() + traceCanvas.HORIZONTAL_GAP / 2, arrow.y);
            try{
                Thread.sleep(delay);
            }
            catch(Exception exception) {}
        }
    }

    public void setDelay(int i){
        delay = i * 1000;
    }

    public int getDelay(){
        return delay / 1000;
    }

}
