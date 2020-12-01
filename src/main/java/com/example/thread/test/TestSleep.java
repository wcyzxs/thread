package com.example.thread.test;

import lombok.extern.slf4j.Slf4j;

/**
 * Author: weichuanyu
 * Time: 2020/12/1 23:14
 * 总结： (1)线程调用sleep方法后，会进入Timed-Waiting(阻塞)状态
 *        (2)可以使用TimeUnit工具类代替Thread.sleep()，TimeUnit.SECONDS.sleep(1)表示休眠1s,可读性更强
 *        (3)打断正在睡眠的线程，会抛出java.lang.InterruptedException: sleep interrupted异常
 */
@Slf4j
public class TestSleep {
    public static void main(String[] args) {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("wake up....");
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        log.info("t1 state: {}",t1.getState());   // t1 state: RUNNABLE   之所以不是Timed-Waiting，是因为主线程执行完，可能t1线程还没进入休眠状态

//        //1.测试线程睡眠
//        try {
//            //主线程休眠500ms，为了让t1线程进入到休眠状态，方便我们进一步查看sleep后线程的状态
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        log.info("t1 state:{}", t1.getState());  // t1 state:TIMED_WAITING

        //2.测试打断正在睡眠的线程，会抛出java.lang.InterruptedException: sleep interrupted
        try {
            Thread.sleep(1000);  //可以使用TimeUnit工具类代替Thread.sleep()，TimeUnit.SECONDS.sleep(1)表示休眠1s,可读性更强

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("interrupt....");
        t1.interrupt();

    }
}
