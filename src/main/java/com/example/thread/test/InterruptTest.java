package com.example.thread.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName
 * @Description   测试打断
 * interrupt可以打断阻塞得线程，也可以打断正在运行得线程
 * thread.isInterrupted()获取线程是否被打断（boolean），如果打断得是sleep,wait,join，那么打断后会清除标记，置为false,意味着没被打断过；若打断正在执行得线程，则interrupted是true。
 * 后面可以利用该返回值，知道当前线程是否正在运行或者是否有必要停止当前线程
 * @Autor wcy
 * @Date 2020/12/3 11:00
 */
@Slf4j
public class InterruptTest {
    public static void main(String[] args) throws  InterruptedException{
        //测试打断阻塞得线程
        test1();
        //测试打断正在执行得线程
        //test2();
    }

    public static void test1() throws InterruptedException{
        Thread thread = new Thread(() -> {
            log.debug("sleep...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");

        thread.start();
        Thread.sleep(2000);
        log.debug("interrupt...");
        thread.interrupt();
        log.debug("线程状态:{}",thread.getState());
        log.debug("打断标记:{}",thread.isInterrupted());
    }


    public static void test2()throws  InterruptedException{
        Thread t1 = new Thread(() -> {
            while (true) {
                //log.debug("正在执行任务");
                boolean interrupted = Thread.currentThread().isInterrupted();
                if(interrupted){
                    log.debug("线程被打断了，退出循环");
                    break;
                }
            }
        }, "t1");

        t1.start();
        Thread.sleep(1000);
        log.debug("执行interrupt。。。。");
        t1.interrupt();


    }

}