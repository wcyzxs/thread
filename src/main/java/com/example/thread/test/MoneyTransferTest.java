package com.example.thread.test;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @ClassName
 * @Description 模拟银行转账测试
 * @Autor wcy
 * @Date 2020/12/8 13:20
 */
@Slf4j
public class MoneyTransferTest {

    static Random random = new Random();

    public static  int getRandomMath(int math){
        return random.nextInt(math)+1;
    }

    public static void main(String[] args) throws InterruptedException{
        Account a = new Account(1000);
        Account b = new Account(1000);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i <1000 ; i++) {
                a.transfer(b, getRandomMath(100));
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i <1000 ; i++) {
                b.transfer(a, getRandomMath(100));
            }

        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        //查看转账后，两个账户得总金额是否等于2000
        log.debug("total:{}",a.getMoney()+b.getMoney());
    }
}


//账户
class Account{
    private int money;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Account(int money) {
        this.money = money;
    }

    /**
     *
     * @param target 目标账户
     * @param amount 转账金额
     * 该方法内this.money是共享变量  target.getMoney()也是共享变量，所以如果使用 syschronized(this) if(this.money > amount){}得话，仅仅只是对this.money加锁
     * 考虑到this.money与target.getMoney()其实都是共享Account对象，所以我们只需要锁住account对象即可(不过这种当账户特别多，之间转换时，比较影响性能，后续再改进)
     *
     *
     */
    public void transfer(Account target,int amount){
        synchronized (Account.class){
            if(this.money > amount){
                this.setMoney(money-amount);
                target.setMoney(target.getMoney()+amount);
            }
        }
    }

}