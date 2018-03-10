package com.yjb.language.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 为每一个客户端使用一个线程，如果客户端出现延时等异常，线程可能会被占用很长时间。
 * 因为数据的准备和读取都在这个线程中。此时，如果客户端数量众多，可能会消耗大量的系统资源。
 */
public class BioServer {

    public static void main(String[] args) throws Exception {
        Executor tp = Executors.newCachedThreadPool();

        ServerSocket echoServer = null;
        try {
            echoServer = new ServerSocket(8000);
        } catch (IOException e) {
            System.out.println(e);
        }

        while (true) {
            try {
                Socket clientSocket = echoServer.accept();
                System.out.println(clientSocket.getRemoteSocketAddress() + " connect!");
                tp.execute(new HandleMsg(clientSocket));
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    static class HandleMsg implements Runnable {

        private Socket clientSocket;
        private BufferedReader is;
        private PrintWriter os;

        HandleMsg(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        // 省略部分信息
        public void run() {
            try {
                is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                os = new PrintWriter(clientSocket.getOutputStream(), true);
                // 从InputStream当中读取客户端所发送的数据
                String inputLine = null;
                long b = System.currentTimeMillis();
                while ((inputLine = is.readLine()) != null) { // 此时如果客户端的socket关闭了就会出现 java.net.SocketException: Connection reset
                    os.println(inputLine);
                }
                long e = System.currentTimeMillis();
                System.out.println("spend:" + (e - b) + " ms ");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //  关闭资源
            }
        }
    }
}
