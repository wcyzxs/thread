package com.example.thread.test;

import com.example.thread.util.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import java.util.LinkedList;

/**
 * Author: chuanyu.wei
 * Time: 2020/12/28 22:24
 **/
@Slf4j
public class MessageQueueTest {
    public static void main(String[] args) {
        MessageQueue messageQueue = new MessageQueue(2);
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                try {
                    messageQueue.put(new Message(id, "消息"+id));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "生产者" + i).start();
        }
        // 1 个消费者线程, 处理结果
        new Thread(() -> {
            while (true) {
                ApiUtils.sleep(1);
                messageQueue.take();
            }
        }, "消费者").start();
    }
}

//消息队列类，java线程间通信
@Slf4j
class MessageQueue{
    //定义消息队列
    private LinkedList<Message> list = new LinkedList<>();
    //最大容量
    private int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    public  Message take(){
        synchronized (list) {
            while (list.isEmpty()) {
                try {
                    log.debug("队列为空，生产者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message message = list.removeFirst();
            log.debug("消费消息：{}",message);
            list.notifyAll();
            return message;
        }
    }

    public  void put(Message message){
        synchronized (list){
            while(list.size() == capacity){
                try {
                    log.debug("队列已满，生产者线程等待。。。");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //队列末尾添加信息
            list.addLast(message);
            log.debug("已生产消息:{}",message);
            list.notifyAll();
        }
    }
}


class Message{
    private int id;

    private Object value;

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}