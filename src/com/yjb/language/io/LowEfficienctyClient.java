package com.yjb.language.io;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

public class LowEfficienctyClient {

    private static final int sleep_time = 1000 * 1000 * 1000;
    private static ExecutorService tp = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        new Thread(new EchoClient()).start();
    }

    public static class EchoClient implements Runnable {
        public void run() {
            try {
                Socket client = new Socket();
                client.connect(new InetSocketAddress("localhost", 8000));
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                writer.print("H");
                LockSupport.parkNanos(sleep_time);
                writer.print("e");
                LockSupport.parkNanos(sleep_time);
                writer.print("l");
                LockSupport.parkNanos(sleep_time);
                writer.print("l");
                LockSupport.parkNanos(sleep_time);
                writer.print("o");
                LockSupport.parkNanos(sleep_time);
                writer.print("!");
                LockSupport.parkNanos(sleep_time);
                writer.println();
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
