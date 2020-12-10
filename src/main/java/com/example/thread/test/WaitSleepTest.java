package com.example.thread.test;

import com.example.thread.util.ApiUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName
 * @Description wait和sleep区别测试
 * 1) sleep 是 Thread 方法，而 wait 是 Object 的方法
 * 2) sleep 不需要强制和 synchronized 配合使用，但 wait 需要和 synchronized 一起用
 * 3) sleep 在睡眠的同时，不会释放对象锁的，但 wait 在等待的时候会释放对象锁(让其他线程获取锁)
 * 4) 它们状态 TIMED_WAITING
 * @Autor wcy
 * @Date 2020/12/10 10:25
 */
@Slf4j
public class WaitSleepTest {
    static Object object = new Object();
    public static void main(String[] args) {

        new Thread(()->{
            log.debug("获得锁对象");
            synchronized (object){
                try {
                    object.wait();  //调用wait会释放锁，主线程的日志是可以打印出来的
                    log.debug("执行业务");
                   // ApiUtils.sleep(10); //不会释放锁，主线程休眠1s,所以t1线程获取锁，睡眠期间不会释放锁，主线程不会获取到锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t1").start();

        ApiUtils.sleep(1);
        synchronized (object){
         log.debug("主线程获取锁");
         object.notify();
        }
    }
}