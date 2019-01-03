package com.github.davidji80.maven.multithread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ThreadImpCallable implements Callable<Integer> {

    @Override
    public Integer call() {
        System.out.println("当前线程名称是：" + Thread.currentThread().getName());

        int i = 0;
        for (; i < 5; i++) {
            System.out.println("循环变量i的值：" + i);
        }

        // call()方法有返回值
        return i;
    }

    public static void main(String[] args) {
        ThreadImpCallable rt = new ThreadImpCallable();

        // 使用FutureTask来包装Callable对象
        FutureTask<Integer> task = new FutureTask<>(rt);
        new Thread(task, "有返回值的线程").start();
        try {
            // 获取线程返回值
            System.out.println("子线程的返回值：" + task.get());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}