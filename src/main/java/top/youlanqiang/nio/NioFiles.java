package top.youlanqiang.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFiles {


    public static void main(String[] args) throws Exception{
        // 使用nio读取文件
        FileInputStream fis = new FileInputStream("NioTest2.txt");
        FileChannel fileChannel = fis.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        fileChannel.read(byteBuffer);

        byteBuffer.flip();
        while(byteBuffer.remaining()>0){
            byte b = byteBuffer.get();
            System.out.print((char) b);
        }
        fis.close();
    }

    // 使用nio写文件
    public void test1() throws IOException {
        FileOutputStream fos = new FileOutputStream("NioTest3.txt");
        FileChannel fileChannel = fos.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.wrap("Hello,NioTest3".getBytes());
        fileChannel.write(byteBuffer);

        fos.close();
    }

}
