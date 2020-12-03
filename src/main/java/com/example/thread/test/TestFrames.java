package com.example.thread.test;

/**
 * @ClassName
 * @Description
 * @Autor wcy
 * @Date 2020/11/30 15:13
 * 测试栈帧 (debug模式下，查看Frames)
 * 可以查看到栈帧的保存数据(包含局部变量，方法参数args,返回地址(记录当前方法执行完之后的返回地址),锁记录，操作数栈)，当执行完之后，会释放栈帧内存；
 * 数据结构：后进先出
 */
public class TestFrames {
    public static void main(String[] args) {
        method1(10);
    }

    private static void method1(int x){
            int y = x+1;
            Object m = method2();
        System.out.println(m);
    }

    private static Object method2(){
        return new Object();
    }
}
