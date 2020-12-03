package com.example.thread.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName
 * @Description
 * @Autor wcy
 * @Date 2020/12/3 18:21
 */
@Slf4j
public class TeaTest {
    public static void main(String[] args) throws InterruptedException {
        log.debug("执行开始...");
        test1();
        test2();
        Thread.sleep(5000);
        log.debug("执行结束...");
    }

    public static void test1(){
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t1.start();
        log.debug("t1执行结束...");
    }

    public static void test2(){
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2");
        t2.start();
        log.debug("t2执行结束...");
    }
}