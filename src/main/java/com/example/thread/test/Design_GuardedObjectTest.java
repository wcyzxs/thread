package com.example.thread.test;

import com.example.thread.util.ApiUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName
 * @Description 多线程模式之保护线程
 * 两个线程间交互解耦
 * 优点：(1)虽然join也能实现，但是另一个线程需要等join的线程执行完才可以后续的事情。而保护性暂停模式线程2可以继续做些别的事，不一定非要等t1执行完
 *       (2)join等待线程里面的变量只能是全局的，但是该模式情况下等待的变量可以是局部的。
 *
 * @Autor wcy
 * @Date 2020/12/10 14:51
 */
@Slf4j
public class Design_GuardedObjectTest {
    public static void main(String[] args) {
        GuardedObject guardedObject = new GuardedObject();
        new Thread(()->{
            //等待结果
            log.debug("等待结果");
          //  String s = guardedObject.get();
            String s = guardedObject.get(2000L);
            log.debug("结果是:{}",s);

        },"t1").start();

        new Thread(()->{
            //等待结果
            log.debug("产生结果");
           // ApiUtils.sleep(1);  //测试超时效果，1s后即可输出
            guardedObject.complete("design test");
        },"t2").start();
    }
}


class GuardedObject{
    private String response;
    private final Object object = new Object();

    /**
     * 获取结果
     * @return
     */
    public String get(){
        synchronized (object){
            //条件不满足，继续等待
            while (response == null){
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }


    /**
     * 设置最大等待时间
     * @param timesOut 表示要等待多久  2s
     * @return
     */
    public String get(Long timesOut){
        synchronized (object){
            //开始时间    15:00:00
            long beginTimes = System.currentTimeMillis();
            long passTimes = 0;
            while(response == null){
                //这一轮循环应该等待得时间
                long waitTime = timesOut - passTimes;
                //经历得时间超出了最大得等待时间，退出循环
                if(waitTime <= 0){
                    break;
                }
                try {
                    object.wait(timesOut);   //防止虚假唤醒   比如15:00:01时唤醒，那么经历得时间就是1s,但是response是null,所以会走下面代码，不会break,因为1s<times得2s,
                                    // 这个时候wait得时间其实应该是只能等待1s了，即times-passTimes
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //经历得时间   15:00:02 - 15:00:00  = 2s
                 passTimes = System.currentTimeMillis() - beginTimes;
            }
        }
        return response;
    }
    /**
     * 产生结果
     * @param response
     */
    public void complete(String response){
        synchronized (object){
            //条件满足，通知等待线程
            this.response = response;
            object.notifyAll();
        }
    }
}


