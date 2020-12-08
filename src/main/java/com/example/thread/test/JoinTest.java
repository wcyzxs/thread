package com.example.thread.test;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

/**
 * Author: weichuanyu
 * Time: 2020/12/2 22:35
 * 测试join
 */
@Slf4j
public class JoinTest {
    static int a = 0;
    public static void main(String[] args) throws InterruptedException {
        test1();
    }


    public static void test1() throws InterruptedException {
        log.debug("开始");
        Thread t1 = new Thread(() -> {
            log.debug("开始");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.debug("结束");
            a = 10;
        });
        t1.start();
        //log.debug("结果为:{}", a);    //此时输出0，因为主线程执行完了，t1还未执行结束
        t1.join();                     // t1.join(1500);该方法意味着主线程只等待1.5s,1.5s内t1线程未执行完得话，也不再继续等待，此时输出a得值还是0;若join提前结束，则主线程也就提前结束
        log.debug("结果为:{}", a);    //此时输出10，因为相当于主线程内加入了t1线程，需要等t1执行结束后，主线程再继续执行
        log.debug("结束");
    }



}
