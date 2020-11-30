package com.example.thread.controller;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Author: weichuanyu
 * Time: 2020/7/27 22:57
 */
public class TestLock {
    public static void main(String[] args) {
        TestLock2 testLock2 = new TestLock2();
        new Thread(testLock2).start();
        new Thread(testLock2).start();
        new Thread(testLock2).start();
    }
}

class TestLock2 implements Runnable {
    int ticketNums = 10;
    ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            if (ticketNums > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(ticketNums--);
            }
        }

       /* try {
           // lock.lock();
            while(true){
                if(ticketNums > 0){
                    System.out.println(ticketNums-- );
                }
            }
        } finally {
          //  lock.unlock();
        }*/

    }
}
