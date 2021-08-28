package top.youlanqiang.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest3 {

    public static void main(String[] args) throws Exception{
        FileOutputStream fos = new FileOutputStream("NioTest3.txt");
        FileChannel fileChannel = fos.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.wrap("Hello,NioTest3".getBytes());
        fileChannel.write(byteBuffer);

        fos.close();
    }
}
