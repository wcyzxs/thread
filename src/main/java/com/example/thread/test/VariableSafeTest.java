package com.example.thread.test;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * @ClassName
 * @Description 变量安全测试
 * @Autor wcy
 * @Date 2020/12/4 15:45
 */
@Slf4j
public class VariableSafeTest {
    public static void main(String[] args) {
        //测试成员变量    线程不安全
/*        NumberVariable numberVariable = new NumberVariable();
        for (int i = 0; i <2 ; i++) {
             new Thread(()->{
                numberVariable.method1(200);
            },"Thread"+(i+1)).start();
        }*/

        LocalVariable localVariable = new LocalVariable();
        //SonLocalVariable localVariable = new SonLocalVariable();   //测试暴露共享变量
        for (int i = 0; i <2 ; i++) {
            new Thread(()->{
                localVariable.method1(200);
            },"Thread"+(i+1)).start();
        }


    }
}

/**
 * 多线程-局部变量
 * 1.每个线程都有自己私有得栈帧，局部变量是存在栈帧中得，是线程私有得；所以每个线程执行这段代码都是互不影响得。
 * 2.不过存在是当局部变量暴露给外部得时候，就会存在线程安全问题。
 */
class LocalVariable{

    public void method1(int loopNumber){
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }
    }

    private void method2(ArrayList<String> list){
        list.add("1");
    }

    public void method3(ArrayList<String> list){
        list.remove(0);
    }

}

/**
 * 暴露共享变量list
 * 子类继承父类，将method3改为public修饰，重新方法。并在方法中新启动一个线程,这样当测试得时候，父类和字类相当于开启了两个线程，但是对同一个list进行了读写操作，会出现线程安全问题。
 *  若想解决这种暴露共享变量，可以将父类得方法加个final修饰，此时字类则无法再次重写；或者采用private修饰，这样子类则无法访问父类得方法
 */
class SonLocalVariable extends  LocalVariable{
    @Override
    public void method3(ArrayList<String> list) {
        new Thread(()->{
            list.remove(0);
        }).start();
    }
}

/**
 *多线程-成员变量
 * list创建之后，是在堆中得，堆中得数据是共享得；所以多个线程对list读写操作，都是对堆中得这个list得操作，是会存在线程安全问题，
 */
class NumberVariable{
    ArrayList<String> list = new ArrayList<String>();

    public void method1(int loopNumber){
        for (int i = 0; i < loopNumber; i++) {
            method2();
            method3();
        }
    }

    private void method2(){
        list.add("1");
    }

    private void method3(){
        list.remove(0);
    }
}

