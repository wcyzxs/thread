package com.example.thread.test;

import com.example.thread.util.ApiUtils;
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
        test1();
    }

    public static void test1() throws InterruptedException{
        Thread t1 = new Thread(() -> {
            log.debug("洗水壶");
            ApiUtils.sleep(1);
            log.debug("烧开水");
            ApiUtils.sleep(5);
        }, "t1");


        Thread t2 = new Thread(() -> {
            log.debug("洗茶壶");
            ApiUtils.sleep(1);
            log.debug("洗茶杯");
            ApiUtils.sleep(2);
            log.debug("拿茶叶");
            ApiUtils.sleep(1);
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("泡茶开始....");
        }, "t2");

        t1.start();
        t2.start();

    }
}