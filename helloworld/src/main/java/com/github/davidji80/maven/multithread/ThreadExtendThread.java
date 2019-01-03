package com.github.davidji80.maven.multithread;

/**
 * 通过继承Thread类来创建线程类
 */
public class ThreadExtendThread extends Thread {
    private int i;

    @Override
    public void run() {
        // 当线程类继承Thread类时，直接使用this即可获取当前线程句柄。
        // 因此可以直接调用getName()方法返回当前线程的名称。
        System.out.println("当前线程名称是：" + getName());

        for (; i < 5; i++) {
            System.out.println(getName() + ":" + i);
            try {
                // 保证让别的线程也有执行的机会
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) {
        // 静态方法没有this，只能通过Thread.currentThread获取当前线程句柄
        System.out.println(Thread.currentThread().getName());

        // 创建、并启动第一条线程
        new ThreadExtendThread().start();
        // 创建、并启动第二条线程
        new ThreadExtendThread().start();
    }
}
