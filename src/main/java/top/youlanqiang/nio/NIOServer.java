package top.youlanqiang.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {


    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel -> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到一个Selector对象
        Selector selector = Selector.open();

        // 绑定一个端口 6666 在服务器端监听
        serverSocketChannel.bind(new InetSocketAddress(6666));

        // 设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 将 serverSocketChannel注册到 selector 事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        while(true){

            // 等待一秒,如果没有事件发生，就继续
            if(selector.select(1_000) == 0){ // 没有事件发生
                System.out.println("服务器等待了1秒,无连接");
                continue;
            }

            // 如果返回>0,就获取到相关的selectionKey集合

            // 返回关注事件的集合
            // 通过selectionKeys 反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 迭代器遍历集合 迭代器可以安全,方便的remove元素
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                if(key.isAcceptable()){ // 如果是 OP_ACCEPT; 表示有新的客户端连接
                    // 客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 配置为非阻塞模型
                    socketChannel.configureBlocking(false);

                    System.out.println("客户端连接成功,生成了一个 socketChannel hashcode:"+socketChannel.hashCode());
                    // 将当前Socket注册到Selector中 关注事件为READ,同时关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }
                if(key.isReadable()){ // 如果是 OP_READ; 可读事件
                    //通过 key来反向获取 对应的channel
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    // 获取到该channel关联的buffer
                    ByteBuffer buffer =  (ByteBuffer) key.attachment();
                    socketChannel.read(buffer);
                    System.out.println("客户端发送:"+ new String(buffer.array()));
                }
                // 手动从集合中移除当前的selectionKey,防止重复操作
                iterator.remove();
            }
        }

    }

}
