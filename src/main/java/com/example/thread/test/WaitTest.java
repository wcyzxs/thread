package com.example.thread.test;

import com.example.thread.util.ApiUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Author: weichuanyu
 * Time: 2020/12/9 23:29
 * //必须要获取到对象的锁，成为Owner才能调用wait;否则会报错
 */
@Slf4j
public class WaitTest {
    static  final Object object = new Object();
    public static void main(String[] args) {
       new Thread(() -> {
            synchronized (object) {
                log.debug("t1执行任务....");
                try {
                    object.wait();
                    //object.wait(3000);   // 有时效的等待，3s后若未被唤醒，则继续向下执行代码
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("t1的其他代码....");
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (object) {
                log.debug("t2执行任务....");
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("t2的其他代码....");
            }
        }, "t2").start();

        //主线程2s后执行
        ApiUtils.sleep(2);
        log.debug("唤醒obj上的其他线程");
        synchronized (object){
            object.notify();  //唤醒obj上一个线程
        }

    }
}
