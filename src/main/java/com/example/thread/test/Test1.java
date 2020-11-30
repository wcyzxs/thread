package com.example.thread.test;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * Author: weichuanyu
 * Time: 2020/11/29 23:17
 */
@Slf4j(topic = "c.Test1")
public class Test1 {

    public static void main(String[] args) {

        Thread thread = new Thread("t1"){
            @Override
            public void run() {
               log.debug("running");
            }
        };
        thread.start();

    }

}
