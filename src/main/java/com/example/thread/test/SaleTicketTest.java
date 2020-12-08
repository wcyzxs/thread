package com.example.thread.test;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * @ClassName
 * @Description 售票案例
 * 注意：哪些是需要考虑线程安全得
 * @Autor wcy
 * @Date 2020/12/8 9:35
 */
@Slf4j
public class SaleTicketTest {
    static Random random = new Random();

    public static  int getRandomMath(int math){
        return random.nextInt(math)+1;
    }
    public static void main(String[] args) {

        TicketWindow ticketWindow = new TicketWindow(1000);

        //统计买票，使用线程安全得方式 Vector得add方法底部实现加了synchronized
        List<Integer> amountList = new Vector<>();

        //所有线程集合
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i <4000 ; i++) {
            Thread thread = new Thread(() -> {
                //买票
                int amount = ticketWindow.sale(getRandomMath(5));
                //add多个线程都会操作，所以需要用Vector
                amountList.add(amount);     // 上面得sale也是会存在线程安全问题，这两个代码组合在一起不需要考虑安全问题，因为window和amountList是两个不同得共享变量

            });
            threadList.add(thread);   //该list是被主线程所操作，不会多多个线程所操作，所以不会存在线程安全问题，定义为ArrayList即可
            thread.start();
        }

        threadList.forEach(t->{
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //统计卖出得票数和剩余票数(需要等上面得线程全部执行结束方可进行统计)
        log.debug("剩余票数:{}",ticketWindow.getCount());
        log.debug("卖出得票数:{}",amountList.stream().mapToInt(i->i).sum());
    }
}


class TicketWindow{
    private int count;

    public  int getCount(){
        return this.count;
    }

    public synchronized  int sale(int num){
        if(this.count < num){
            return 0;
        }else{
            this.count -= num;
            return num;
        }
    }

    public TicketWindow(int count) {
        this.count = count;
    }


}