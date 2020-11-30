package com.example.thread.controller;

/**
 * Author: weichuanyu
 * Time: 2020/7/23 22:22
 */
public class ThreadController implements Runnable {
    private String winner;

    @Override
    public void run() {

        for (int i = 0; i <= 100; i++) {
            boolean flag = gameWinner(i);
            if (flag) {
                break;
            }
            System.out.println(Thread.currentThread().getName() + "跑了第" + i + "步");
        }
    }

    public boolean gameWinner(int i) {
        if (winner != null) {
            return true;
        }
        boolean flag = false;
        if (i >= 100) {
            winner = Thread.currentThread().getName();
            System.out.println(winner + "is winner");
            flag = true;
        }
        return flag;
    }

    public static void main(String[] args) {
        ThreadController threadController = new ThreadController();

        new Thread(threadController, "乌龟").start();
        new Thread(threadController, "兔子").start();
    }
}
