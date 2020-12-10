package com.example.thread.test;

import com.example.thread.util.ApiUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Author: weichuanyu
 * Time: 2020/12/9 23:29
 * //必须要获取到对象的锁，成为Owner才能调用wait(包括调用notify()和notifyAll()也是如此);否则会报错IllegalMonitorStateException
 * notify()唤醒Monitor中WaitSet中的一个线程
 * notifyAll()唤醒Monitor中WaitSet中的所有线程
 * 虚假唤醒：即有多个线程在等待的时候，调用notify并不是唤醒了我们所期待的那个线程，也就是错误唤醒/虚假唤醒
 */
@Slf4j
public class WaitNotifyTest {
    static  final Object object = new Object();
    private static  boolean tea = false;
    private static boolean milk = false;


    public static void main(String[] args) {

/*      //测试wait&&sleep区别
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
        ApiUtils.sleep(1);
        log.debug("唤醒obj上的其他线程");
        synchronized (object){
            object.notify();  //唤醒obj上一个线程
        }*/

    //测试wait与notify的使用
    test2();
    ApiUtils.sleep(1);
        synchronized (object){
            tea = true;
            log.debug("主线程执行，准备唤醒等待线程");
            object.notifyAll();
        }
    }


    /**
     * 版本1：调用wait前，if判断
     *11:27:46.239 [小张] DEBUG com.example.thread.test.WaitTest - 没有茶叶，我睡一会
     *11:27:46.241 [小李] DEBUG com.example.thread.test.WaitTest - 没有牛奶，我睡一会
     *11:27:47.238 [main] DEBUG com.example.thread.test.WaitTest - 主线程执行，准备唤醒等待线程
     *11:27:47.238 [小李] DEBUG com.example.thread.test.WaitTest - 牛奶还没有来，暂时不干活
     *11:27:47.238 [小张] DEBUG com.example.thread.test.WaitTest - 茶来了，可以开始干活了
     * 总结：虽然全部被唤醒了，但是小李并没法执行后续任务，因为milk还是flase,这样相当于小李的唤醒是没什么意义的
     *
     * 版本2：调用wait前，while判断
     * 11:29:46.933 [小张] DEBUG com.example.thread.test.WaitTest - 没有茶叶，我睡一会
     * 11:29:46.935 [小李] DEBUG com.example.thread.test.WaitTest - 没有牛奶，我睡一会
     * 11:29:47.933 [main] DEBUG com.example.thread.test.WaitTest - 主线程执行，准备唤醒等待线程
     * 11:29:47.933 [小李] DEBUG com.example.thread.test.WaitTest - 没有牛奶，我睡一会
     * 11:29:47.933 [小张] DEBUG com.example.thread.test.WaitTest - 茶来了，可以开始干活了
     * 总结：虽然调用了notifyAll，但是由于是while循环，小李判断了牛奶是否为true,如果是false,那么他继续等待，这样就不会存在版本1中存在的唤醒无意义的问题了
     * 所以推荐使用版本2
     */
    public static void test2(){
        new Thread(()->{
           synchronized (object){
              while (!tea){
                   log.debug("没有茶叶，我睡一会");
                   try {
                       object.wait();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }
           // if(tea){
                log.debug("茶来了，可以开始干活了");
           // }else{
           //     log.debug("茶还没有来，暂时不干活");
           // }
        },"小张").start();

        new Thread(()->{
            synchronized (object){
                while (!milk){
                    log.debug("没有牛奶，我睡一会");
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
           //if(milk){
                log.debug("牛奶来了，可以开始干活了");
           // }else{
            //    log.debug("牛奶还没有来，暂时不干活");
           // }

        },"小李").start();
    }
}
