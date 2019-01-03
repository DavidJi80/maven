package com.github.davidji80.maven.multithread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class FutureDemo {
    static int index = 0;

    public static class RunnableTask implements Runnable {

        @Override
        public void run() {
            System.out.println("RunnableTask:" + index);
            index++;
        }
    }

    public static class CallableTask implements Callable<String> {

        @Override
        public String call() throws Exception {
            index++;
            System.out.println("exec:" + index+","+new Date().getTime());
            return "result:" + index+","+new Date().getTime();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        callableTask();
        //runnableTask();
    }

    private static void callableTask() throws ExecutionException, InterruptedException {
        List<Future<String>> results = new ArrayList<>();
        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            results.add(es.submit(new CallableTask()));
        }
        for (Future<String> result : results) {
            System.out.println(result.get());
        }
    }

    private static void runnableTask() {
        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            es.submit(new RunnableTask());
        }
    }
}
