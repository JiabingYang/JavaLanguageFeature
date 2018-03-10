package com.yjb.language.io;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NIO有一个很大的特点就是：把数据准备好了再通知我。
 * <p>
 * Channel有点类似于流，一个Channel可以和文件或者网络Socket对应。
 * <p>
 * selector是一个选择器，它可以选择某一个Channel，然后做些事情。
 * 一个线程可以对应一个selector，而一个selector可以轮询多个Channel，而每个Channel对应了一个Socket。
 * <p>
 * 与BIO一个线程对应一个Socket相比，使用NIO后，一个线程可以轮询多个Socket。
 * <p>
 * 当selector调用select()时，会查看是否有客户端准备好了数据。当没有数据被准备好时，select()会阻塞。平时都说NIO是非阻塞的，但是如果没有数据被准备好还是会有阻塞现象。
 * 当有数据被准备好时，调用完select()后，会返回一个SelectionKey，SelectionKey表示在某个selector上的某个Channel的数据已经被准备好了。
 * 只有在数据准备好时，这个Channel才会被选择。
 * <p>
 * 这样NIO实现了一个线程来监控多个客户端。
 * <p>
 * 而刚刚模拟的网络延迟的客户端将不会影响NIO下的线程，因为某个Socket网络延迟时，数据还未被准备好，selector是不会选择它的，而会选择其他准备好的客户端。
 * <p>
 * selectNow()与select()的区别在于，selectNow()是不阻塞的，当没有客户端准备好数据时，selectNow()不会阻塞，将返回0，有客户端准备好数据时，selectNow()返回准备好的客户端的个数。
 * <p>
 * 总结：
 * 1. NIO会将数据准备好后，再交由应用进行处理，数据的读取/写入过程依然在应用线程中完成，只是将等待的时间剥离到单独的线程中去。
 * 2. 节省数据准备时间（因为Selector可以复用）
 */
public class MultiThreadNioServer {

    private static Map<Socket, Long> geymTimeStat = new HashMap<>();
    private Selector selector;
    private ExecutorService tp = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        MultiThreadNioServer server = new MultiThreadNioServer();
        try {
            server.startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startServer() throws Exception {
        selector = SelectorProvider.provider().openSelector();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(8000));
        // 注册感兴趣的事件，此处对accpet事件感兴趣
        SelectionKey acceptKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        for (; ; ) {
            selector.select();
            Set readyKeys = selector.selectedKeys();
            Iterator i = readyKeys.iterator();
            while (i.hasNext()) {
                SelectionKey selectionKey = (SelectionKey) i.next();
                i.remove();
                if (selectionKey.isAcceptable()) {
                    doAccept(selectionKey);
                    continue;
                }
                if (selectionKey.isValid() && selectionKey.isReadable()) {
                    if (!geymTimeStat.containsKey(((SocketChannel) selectionKey.channel()).socket())) {
                        geymTimeStat.put(((SocketChannel) selectionKey.channel()).socket(), System.currentTimeMillis());
                    }
                    doRead(selectionKey);
                    continue;
                }
                if (selectionKey.isValid() && selectionKey.isWritable()) {
                    doWrite(selectionKey);
                    long e = System.currentTimeMillis();
                    long b = geymTimeStat.remove(((SocketChannel) selectionKey.channel()).socket());
                    System.out.println("spend:" + (e - b) + "ms");
                }
            }
        }
    }

    private void doWrite(SelectionKey sk) {
        SocketChannel channel = (SocketChannel) sk.channel();
        EchoClient echoClient = (EchoClient) sk.attachment();
        LinkedList<ByteBuffer> outq = echoClient.getOutputQueue();
        ByteBuffer bb = outq.getLast();
        try {
            int len = channel.write(bb);
            if (len == -1) {
                disconnect(sk);
                return;
            }
            if (bb.remaining() == 0) {
                outq.removeLast();
            }
        } catch (Exception e) {
            disconnect(sk);
        }
        if (outq.size() == 0) {
            sk.interestOps(SelectionKey.OP_READ);
        }
    }

    private void doRead(SelectionKey selectionKey) {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        int len;
        try {
            len = channel.read(buffer);
            if (len < 0) {
                disconnect(selectionKey);
                return;
            }
        } catch (Exception e) {
            disconnect(selectionKey);
            return;
        }
        buffer.flip();
        tp.execute(new HandleMsg(selectionKey, buffer));
    }

    private void disconnect(SelectionKey sk) {
        //省略略干关闭操作
    }

    private void doAccept(SelectionKey selectionKey) {
        ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel clientChannel;
        try {
            clientChannel = serverChannel.accept();
            clientChannel.configureBlocking(false);
            SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);
            clientKey.attach(new EchoClient());
            InetAddress clientAddress = clientChannel.socket().getInetAddress();
            System.out.println("Accepted connection from " + clientAddress.getHostAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class EchoClient {
        private LinkedList<ByteBuffer> outputQueue;

        EchoClient() {
            outputQueue = new LinkedList<>();
        }

        LinkedList<ByteBuffer> getOutputQueue() {
            return outputQueue;
        }

        void enqueue(ByteBuffer bb) {
            outputQueue.addFirst(bb);
        }
    }

    class HandleMsg implements Runnable {
        SelectionKey selectionKey;
        ByteBuffer byteBuffer;

        HandleMsg(SelectionKey selectionKey, ByteBuffer byteBuffer) {
            super();
            this.selectionKey = selectionKey;
            this.byteBuffer = byteBuffer;
        }

        @Override
        public void run() {
            EchoClient echoClient = (EchoClient) selectionKey.attachment();
            echoClient.enqueue(byteBuffer);
            selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            selector.wakeup();
        }
    }
}
