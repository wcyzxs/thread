package com.example.thread.test;

import lombok.extern.slf4j.Slf4j;

/**
 * Author: weichuanyu
 * Time: 2020/11/29 23:24
 * 以下是Thread(target,name)的源码，当runnable不为空时，就执行其run方法
 *    @Override
 *     public void run() {
 *         if (target != null) {
 *             target.run();
 *         }
 *     }
 */
@Slf4j
public class RunnableTest {

    public static void main(String[] args) {
/*        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                log.debug("这是任务对象");
            }
        };

         Thread t = new Thread(runnable, "t2");
        t.start();
        */

       Thread t = new Thread(() -> {log.debug("使用lambda表达式构建任务对象");},"t3");
       t.start();
    }
}
