package com.yjb.language.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws Exception {
        try {
            Socket client = new Socket();
            client.connect(new InetSocketAddress("localhost", 8000));
            PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
            writer.println("Hello!");
            writer.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("from server: " + reader.readLine());
        } catch (Exception e) {
        } finally {
            // 省略资源关闭
        }
    }
}
