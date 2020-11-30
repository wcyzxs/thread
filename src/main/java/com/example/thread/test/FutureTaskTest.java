package com.example.thread.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Author: weichuanyu
 * Time: 2020/11/29 23:46
 * FutureTask底层也是继承了Runnable接口以及Future，Future里面是有get方法，可以获取任务的返回值(泛型)
 *
 * log.debug("{}",futureTask.get());  主线程会一直等待，等待call方法执行完成，获取其返回值
 */
@Slf4j
public class FutureTaskTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.创建任务对象 FutureTask能够接收Callable类型的参数，用来处理有返回结果的情况
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("running.....");
                Thread.sleep(5000);
                return 100;
            }
        });
        //2.启动线程
        Thread t = new Thread(futureTask);
        t.start();

        //主线程阻塞，同步等待futureTask执行完毕的结果
        log.debug("{}",futureTask.get());
    }
}
