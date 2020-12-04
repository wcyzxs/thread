package com.example.thread.test;

import com.example.thread.util.ApiUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName
 * @Description 测试synchronized
 * 对于普通同步方法，锁的是当前的实例对象。
 * 对于静态同步方法，锁的是当前类的Class对象。
 * 对于同步方法块，所得是Synchonized括号内配置的对象。
 * @Autor wcy
 * @Date 2020/12/4 10:47
 */
@Slf4j
public class SynchronizedTest {
    public static void main(String[] args) {
        //test1();
       // test2();  //测试结果：  2s后a,b  或者b 2s后a
        test3();    //测试结果：  b 2s后d

    }

    /**
     * 测试synchronized得安全性
     */
    public static void test1(){
        Room room = new Room();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                room.increment();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                room.decrement();
            }
        }, "t2");

        t1.start();
        t2.start();
        log.debug("count得值:{}" ,room.getCount());
    }


    /**
     * 测试synchronized锁得作用域  （测试加在普通方法上面，以及不同类得实例对象锁）
     */
    public static void test2(){
        Number number = new Number();
       // Number number2 = new Number();    //也可以number2.b()测试，这样一来，t1和t2就不会再互斥了
        Thread t1 = new Thread(() -> {
            log.debug("t1 start");
            number.a();
        }, "t1");

        Thread t2 = new Thread(() -> {
            log.debug("t2 start");
            number.b();
        }, "t2");

//        Thread t3 = new Thread(() -> {
//            log.debug("t3 start");
//            number.c();
//        }, "t3");

        t1.start();
        t2.start();
//        t3.start();

    }

    /**
     * 测试synchronized锁得作用域   (锁在静态方法上以及非静态方法)
     * 前者锁类对象，后者锁得是this对象也就是类得实例对象，所以二者不会互斥，输出结果是b 2s后d
     */
    public static void test3(){
        Number number = new Number();
        Number number2 = new Number();
        Thread t1 = new Thread(() -> {
            number.a();
        }, "t1");

        Thread t2 = new Thread(() -> {
            number2.d();
        }, "t2");
        t1.start();
        t2.start();

    }
}


class Room{
    private static int count = 0;

    public void increment(){
        synchronized (this){
            count++;
        }
    }

    public void decrement(){
        synchronized (this){
            count--;
        }
    }

    //该方法和上面等价，锁得都是实例对象
//    public synchronized void decrement(){
//            count--;
//    }

    public int getCount(){
        synchronized (this){
            return count;
        }
    }
}

@Slf4j
class Number{
    public synchronized void a(){
        ApiUtils.sleep(2);
        log.debug("a");
    }

    public synchronized void b(){
        log.debug("b");
    }

    //该方法没有加锁，所以如果t3执行c方法，那么最终有3种情况     (1)c 2s后ab   (2)cb 2s后a    (3)bc  2s后a
    public void c(){
        log.debug("c");
    }

    public static synchronized void d(){
        ApiUtils.sleep(2);
        log.debug("d");
    }
}

