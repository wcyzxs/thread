package com.example.thread.controller;

/**
 * Author: weichuanyu
 * Time: 2020/7/27 22:00
 */
public class UnsafaBank {

    public static void main(String[] args) {
        Account account = new Account(1000, "基金");
        Drawing you = new Drawing(account, 50, "你");
        Drawing girl = new Drawing(account, 100, "girl");
        you.start();
        girl.start();
    }
}

class Account {
    int money;
    String name;

    public Account(int money, String name) {
        this.money = money;
        this.name = name;
    }
}

class Drawing extends Thread {

    Account account;//账户
    int drawMoney; //取钱
    int nowMoney;    //现在手里有多少钱

    public Drawing(Account account, int drawMoney, String name) {
        super(name);
        this.account = account;
        this.drawMoney = drawMoney;
    }

    /**
     * synchronized加在方法上面，默认锁的是方法所属的对象，run是属于Drawing的，其实实际上我们应该锁账号
     */
    @Override
    public void run() {
        synchronized (account) {
            //判断余额是否满足要去的钱
            if (account.money < drawMoney) {
                System.out.println(this.getName() + "钱不够，取不了了");
                return;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            account.money = account.money - drawMoney;
            nowMoney = nowMoney + drawMoney;

            System.out.println(this.getName() + "余额为：" + account.money);
            System.out.println(Thread.currentThread().getName() + "手里的钱：" + nowMoney);
        }

    }
}