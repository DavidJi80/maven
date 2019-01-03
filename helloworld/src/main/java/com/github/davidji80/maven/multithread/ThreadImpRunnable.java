package com.github.davidji80.maven.multithread;

public class ThreadImpRunnable implements Runnable {
    private int i;

    @Override
    public void run() {
        System.out.println("当前线程名称是：" + Thread.currentThread().getName());

        for (; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i);

            try {
                // 因为sleep是静态方法，所以不需要通过Thread.currentThread()方法获取当前线程句柄
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) {
        ThreadImpRunnable st = new ThreadImpRunnable();
        new Thread(st, "Thread1").start();
        new Thread(st, "Thread2").start();
    }
}
