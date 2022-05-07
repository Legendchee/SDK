package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mylibrary.ToastUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static Integer count = 0;
    private final Integer FULL = 5;
    private static String lock = "lock";
    private int mode = 2;
    final Lock lock1 = new ReentrantLock();
    final Condition put = lock1.newCondition();
    final Condition get = lock1.newCondition();
    final BlockingQueue<Integer> bq = new ArrayBlockingQueue<Integer>(5);
    final Semaphore put1 = new Semaphore(5);
    final Semaphore get1 = new Semaphore(0);
    final Semaphore mutex = new Semaphore(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToastUtil.showMsg(this,"puppet:hello!");

        switch (mode) {
            case 0:
                new Thread(this.new Producer()).start();
                new Thread(this.new Consumer()).start();
                new Thread(this.new Producer()).start();
                new Thread(this.new Consumer()).start();
                break;
            case 1:
                new Thread(this.new Producer1()).start();
                new Thread(this.new Consumer1()).start();
                new Thread(this.new Producer1()).start();
                new Thread(this.new Consumer1()).start();
                break;
            case 2:
                new Thread(this.new Producer3()).start();
                new Thread(this.new Consumer3()).start();
                new Thread(this.new Producer3()).start();
                new Thread(this.new Consumer3()).start();
                break;

            case 3:
                new Thread(this.new Producer4()).start();
                new Thread(this.new Consumer4()).start();
                new Thread(this.new Producer4()).start();
                new Thread(this.new Consumer4()).start();
                break;
            default:
                break;
        }

    }

    class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {

                Log.d(TAG, "run: puppet:i=====" + i);
                Log.d(TAG, "run: puppet:Thread.currentThread().getName()====" + Thread.currentThread().getName());

                try {
                    Log.d(TAG, "run: puppet:Thread.sleep(1000)");

                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                synchronized (lock) {
                    while (count == FULL) {
                        try {
                            Log.d(TAG, "run: puppet:Thread.currentThread().getName()====" + Thread.currentThread().getName());

                            Log.d(TAG, "run: puppet:wait111111====");

                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count++;
                    System.out.println("生产者" + Thread.currentThread().getName()
                            + "已生产完成，商品数量：" + count);
                    lock.notifyAll();
                }
            }
        }
    }

    class Consumer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                Log.d(TAG, "run222222: puppet:i=====" + i);

                Log.d(TAG, "run22222: puppet:Thread.currentThread().getName()====" + Thread.currentThread().getName());

                try {
                    Log.d(TAG, "run2222222: puppet:Thread.sleep(1000)");

                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                synchronized (lock) {
                    while (count == 0) {
                        try {
                            Log.d(TAG, "run22222: puppet:Thread.currentThread().getName()====" + Thread.currentThread().getName());

                            Log.d(TAG, "run22222: puppet:wait111111====");

                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count--;
                    System.out.println("消费者" + Thread.currentThread().getName()
                            + "已消费，剩余商品数量：" + count);
                    lock.notifyAll();
                }
            }
        }
    }

    class Producer1 implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {

                Log.d(TAG, "run:Producer1: puppet:i=====" + i);
                Log.d(TAG, "run:Producer1: puppet:Thread.currentThread().getName()====" + Thread.currentThread().getName());


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock1.lock();
                try {
                    while (count == FULL) {
                        try {
                            Log.d(TAG, "run:Producer1: puppet:Thread.currentThread().getName()====" + Thread.currentThread().getName());

                            Log.d(TAG, "run:Producer1: puppet:wait111111====");
                            put.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    count++;
                    System.out.println("生产者" + Thread.currentThread().getName() + "已生产完成，商品数量：" + count);
                    get.signal();
                } finally {
                    lock1.unlock();
                }
            }
        }
    }

    class Consumer1 implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {

                Log.d(TAG, "run222222:Consumer1: puppet:i=====" + i);

                Log.d(TAG, "run22222:Consumer1: puppet:Thread.currentThread().getName()====" + Thread.currentThread().getName());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock1.lock();

                try {
                    while (count == 0) {
                        try {
                            Log.d(TAG, "run22222:Consumer1: puppet:Thread.currentThread().getName()====" + Thread.currentThread().getName());

                            Log.d(TAG, "run22222:Consumer1: puppet:wait111111====");
                            get.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    count--;
                    System.out.println("消费者" + Thread.currentThread().getName() + "已消费，剩余商品数量：" + count);
                    put.signal();
                } finally {
                    lock1.unlock();
                }

            }
        }
    }

    class Producer3 implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    bq.put(1);
                    count++;
                    System.out.println("生产者" + Thread.currentThread().getName()
                            + "已生产完成，商品数量：" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Consumer3 implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    bq.take();
                    count--;
                    System.out.println("消费者" + Thread.currentThread().getName()
                            + "已消费，剩余商品数量：" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Producer4 implements Runnable{
        @Override
        public void run() {
            for(int i=0;i<5;i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    put1.acquire();
                    mutex.acquire();
                    count--;
                    System.out.println("消费者" + Thread.currentThread().getName()
                            + "已消费，剩余商品数量：" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    mutex.release();
                    get1.release();
                }

            }
        }
    }

    class Consumer4 implements Runnable{

        @Override
        public void run() {
            for(int i = 0;i<5;i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    get1.acquire();
                    mutex.acquire();
                    count--;
                    System.out.println("消费者" + Thread.currentThread().getName()
                            + "已消费，剩余商品数量：" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    mutex.release();
                    put1.release();
                }

            }
        }
    }

}