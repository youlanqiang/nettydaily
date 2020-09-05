package top.youlanqiang.nio;

import java.net.InetSocketAddress;
import java.nio.IntBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;
import java.security.SecureRandom;

/**
 * java nio中拥有3个核心概念
 * Selector，Channel，Buffer
 * 在nio中是面向块或缓冲区（Buffer）来编程的
 *
 */
public class NioTest1 {

    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            //SecureRandom是一种比Random更加健壮的随机器
            int randomNumber = new SecureRandom().nextInt(20);
            buffer.put(randomNumber);
        }

        buffer.flip(); //切换为读模式.

        while(buffer.hasRemaining()){//remaining -- 剩余的
            System.out.println(buffer.get());
        }

    }
}
