package com.example.thread.util;

import java.util.concurrent.TimeUnit;

/**
 * Author: weichuanyu
 * Time: 2020/12/3 22:45
 */
public class ApiUtils {
    public static void sleep(int seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
