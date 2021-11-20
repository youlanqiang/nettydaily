package top.youlanqiang.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {


    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务器端的ip和端口
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 6666);

        //连接服务器
        if(!socketChannel.connect(address)){
            while(!socketChannel.finishConnect()){
                System.out.println("正在连接中.");
            }
        }

        // 如果连接成功,就发送数据
        String str = "hello,world.";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());

        // 发送数据
        socketChannel.write(buffer);
        System.in.read();

    }
}
