package com.example.thread.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName
 * @Description 两阶段终止模式Interrupt
 *  1.判断是否被打断，打断则执行后续操作，如释放锁资源等，若未被打断，则休眠2s,再监控是否被打断
 *  2.2s后若发现被打断了，则分两种i情况
 *      (1)不是在睡眠期间打断，是在程序正常执行期间打断，此时监控标记isInterrupted是为true。此时进入下一轮循环时，就会判断被打断，执行后续逻辑。
 *      (2)在睡眠期间打断，会抛出InterruptedException，此时捕获这个异常，并设置打断标记为true,同样得，进入下一轮循环时，就会判断到被打断，并执行后续逻辑。
 * @Autor wcy
 * @Date 2020/12/3 13:54
 */
@Slf4j
public class TwoPhraseInterruptTest {
    public static void main(String[] args) throws  InterruptedException{
        TwoPhrase twoPhrase = new TwoPhrase();
        twoPhrase.start();
        Thread.sleep(3500);
        twoPhrase.stop();
    }
}

@Slf4j
class TwoPhrase{
    private Thread monitor;

    /**
     * 启动监控线程
     */
    public void start(){
        monitor = new Thread(()->{
            while (true){
                Thread currentThread = Thread.currentThread();
                if(currentThread.isInterrupted()){
                    log.debug("线程被打断了，执行后续逻辑");
                    break;
                }
                try {
                    Thread.sleep(1000);  //情况1： 线程休眠期间被打断
                 //   log.debug("状态:{}",currentThread.getState());
                    log.debug("执行监控记录");  //情况2： 执行正常代码，被打断
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //重新设置打断标记为true
                    currentThread.interrupt();
                }
            }
        });
        monitor.start();
    }

    /**
     * 终止监控线程
     */
    public void stop(){
        monitor.interrupt();
        log.debug("状态:{}",Thread.currentThread().getState());
    }
}