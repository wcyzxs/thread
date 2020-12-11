package com.example.thread.test;

import com.example.thread.util.ApiUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Author: weichuanyu
 * Description:多任务版的GuarderObject
 * 如果需要在多个类之间使用 GuardedObject 对象，作为参数传递不是很方便，因此设计一个用来解耦的中间类，这样不仅能够解耦【结果等待者】和【结果生产者】，还能够同时支持多个任务的管理
 * 这里举例：居民，邮递员，邮箱(作为中间解耦类使用)
 * Time: 2020/12/11 23:26
 */
@Slf4j
public class Design_GuardedObjectV2Test {
    public static void main(String[] args) {
        for (int i = 0; i <3 ; i++) {
            new People().start();

        }
        ApiUtils.sleep(1);   //1s后邮递员开始送信
        for (Integer id : MailBoxes.getIds()) {
            new Postman(id, "内容"+id).start();
        }
    }
}

//居民和邮递员都会是多个人，所以设计对应上每一个线程类
@Slf4j
class People extends  Thread{
    @Override
    public void run() {
       //收信
        GuarderObject guarderObject = MailBoxes.createGuarderObject();
        log.debug("开始收信:{}",guarderObject.getId());
        String response = guarderObject.get(5000L);//等5s收信
        log.debug("收到信id:{},内容是:{}",guarderObject.getId(),response);

    }
}

@Slf4j
class Postman extends  Thread {
    private int id; //对应的是GuraderObject的id
    private String response; //发送内容

    public Postman(int id, String response) {
        this.id = id;
        this.response = response;
    }

    @Override
    public void run() {
        //邮箱里面有多个信箱，找到对应的信箱，这里的信箱相当于guarderObject，并发送内容
        GuarderObject guarderObject = MailBoxes.getGuarderObject(id);
        log.debug("送信id:{},内容是:{}",guarderObject.getId(),response);
        guarderObject.complete(response);
    }
}

//中间解耦类(邮箱)
class MailBoxes{
    private static Map<Integer,GuarderObject> boxes = new Hashtable<Integer, GuarderObject>();

    static  int id;
    //产生唯一id
    public static  synchronized  int generateId(){
        return id++;
    }

    //这里和下面的getIds不需要加锁，因为读写的是boxes变量，是线程安全的
    public static GuarderObject createGuarderObject(){
        GuarderObject guarderObject = new GuarderObject(generateId());
        boxes.put(guarderObject.getId(),guarderObject);
        return guarderObject;
    }

    public static Set<Integer> getIds(){
        return boxes.keySet();
    }

    /**
     * 这里用remove原因：当你邮递员给该信箱发送完信息，并且居民收到信之后，以后也就不需要该结果了，所以及时清空，防止内存泄露
     * remove是获取到该对象后就删除，get获取到该对象后不会删除该对象
     * @param id
     * @return
     */
    public static  GuarderObject getGuarderObject(int id){
        return boxes.remove(id);
    }
}

class GuarderObject{

    private int id;

    public int getId() {
        return id;
    }

    public GuarderObject(int id) {
        this.id = id;
    }

    private String response;
    private final Object object = new Object();

    public synchronized String get(Long timesOut){
        long beginTime = System.currentTimeMillis();
        //定义经历时间
        long passTimes = 0;
        synchronized (object){
            while (response == null){
                //如果经历的时间大于最长等待时间，则退出循环
                long waitTime  =   timesOut - passTimes;
                if(waitTime < 0){
                    break;
                }
                try {
                    object.wait(waitTime);   //防止线程被虚假唤醒，即唤醒后response仍然是null,那么继续后下面的代码，但是于此同时
                                            //等待的时间应该是初始等待的最大时长减去上一轮等待的时间。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //经历时间赋值
            passTimes = System.currentTimeMillis() - beginTime;
        }
        return response;
    }

    public  void complete(String response){
        synchronized(object){
            this.response = response;
            //条件满足，通知等待线程
            object.notifyAll();
        }
    }
}
