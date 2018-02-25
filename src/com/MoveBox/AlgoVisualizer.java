package com.MoveBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AlgoVisualizer {


    private GameData data;        // 数据
    private AlgoFrame frame;    // 视图
    private static int DELAY = 5;
    private static int blockSide = 80;

    public AlgoVisualizer(String filename){

        data = new GameData(filename);
        int sceneWidth = data.M() * blockSide;
        int sceneHeight = data.N() * blockSide;

        EventQueue.invokeLater(() -> {
            frame = new AlgoFrame("Move the Box Solver", sceneWidth,sceneHeight);
            frame.addMouseListener(new AlgoMouseListener());
            new Thread(() -> {
                run();
            }).start();
        });
    }

    // 动画逻辑
    private void run(){
        setData(-1, -1);
        if (data.solve()){
            System.out.println("有解");
        }else {
            System.out.println("无解");
        }
        setData(-1, -1);

    }


    private void setData(int clickx, int clicky){
        data.clickx = clickx;
        data.clicky = clicky;

        frame.render(data);
        AlgoVisHelper.pause(DELAY);
    }
    public void addAlgoMouseListener(){
        frame.addMouseListener(new AlgoMouseListener());
    }

    private Position clickPos1 = null;
    private Position clickPos2 = null;
    private class AlgoMouseListener extends MouseAdapter{

        @Override
        public void mouseReleased(MouseEvent event){
            event.translatePoint(
                    -(int)(frame.getBounds().width - frame.getCanvasWidth()),
                    -(int)(frame.getBounds().height - frame.getCanvasHeight())
            );

            Point pos = event.getPoint();
            //System.out.println(pos.x + " , " + pos.y );

            int w = frame.getCanvasWidth() / data.M();
            int h = frame.getCanvasHeight() / data.N();

            int x = pos.y / h;
            int y = pos.x / w;
            System.out.println(x + " , " + y);

            if(SwingUtilities.isLeftMouseButton(event)){
                if(data.inArea(x, y)){
                    setData(x, y);
                    if(clickPos1 == null){
                        clickPos1 = new Position(x, y);
                    }
                    else{
                        clickPos2 = new Position(x, y);
                        if(clickPos2.nextTo(clickPos1)){
                            data.getShowBoard().swap(clickPos1.getX(), clickPos1.getY(), clickPos2.getX(), clickPos2.getY());
                            data.getShowBoard().run();
                        }
                        clickPos1 = null;
                        clickPos2 = null;
                        setData(-1, -1);
                    }
                }
                else{
                    setData(-1, -1);
                    clickPos1 = null;
                    clickPos2 = null;
                }
            }
        }
    }

    public static void main(String[] args) {

        String filename = "level/boston_09.txt";

        AlgoVisualizer vis = new AlgoVisualizer(filename);
    }

}

