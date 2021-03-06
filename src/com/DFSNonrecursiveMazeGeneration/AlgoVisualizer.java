package com.DFSNonrecursiveMazeGeneration;

import java.awt.*;
import java.util.Stack;

public class AlgoVisualizer {

    private static int blockSize = 8;
    private static int DELAY = 5;
    private MazeData data;
    private AlgoFrame frame;
    private static final int d[][] = {{-1,0},{0,1},{1,0},{0,-1}};

    public AlgoVisualizer(int N,int M){
        /* 初始化数据 */
        data = new MazeData(N,M);
        int sceneHeight = data.N() * blockSize;
        int sceneWidth = data.M() * blockSize;

        // 初始化视图
        EventQueue.invokeLater(() -> {
            frame = new AlgoFrame("生成迷宫", sceneWidth, sceneHeight);

            new Thread(() -> {
                run();
            }).start();
        });
    }

    private void run(){
        setData(-1,-1);
        Stack<Position> stack = new Stack<Position>();
        Position first = new Position(data.getEntranceX(),data.getEntranceY()+1);
        stack.push(first);
        data.visited[first.getX()][first.getY()] = true;
        while (!stack.isEmpty()){
            Position curPos = stack.pop();
            for (int i = 0; i < 4; i++){
                int newX = curPos.getX() + d[i][0] * 2;
                int newY = curPos.getY() + d[i][1] * 2;
                if(data.inArea(newX,newY) && !data.visited[newX][newY]){
                    stack.push(new Position(newX, newY));
                    data.visited[newX][newY] = true;
                    setData(curPos.getX()+d[i][0],curPos.getY()+d[i][1]);
                }
            }
        }

        setData(-1,-1);
    }

    private void setData(int x,int y){
        if (data.inArea(x,y)){
            data.maze[x][y] = MazeData.ROAD;
        }
        frame.render(data);
        AlgoVisHelper.pause(DELAY);
    }
    public static void main(String[] args) {
        int N = 101;
        int M = 101;

        AlgoVisualizer vis = new AlgoVisualizer(N,M);

    }
}