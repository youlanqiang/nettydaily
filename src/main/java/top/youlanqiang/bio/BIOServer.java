package top.youlanqiang.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author youlanqiang
 * created in 2021/9/2 23:29
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {

        //1. 创建一个线程池
        //2. 如果有客户端连接，就创建一个线程，与之通信

        ExecutorService executorService = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(66666);
        System.out.println("服务器启动.");

        while(true){
            // 监听等待客户端连接
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            //就创建一个线程，与之通信
            executorService.execute(()->{
                handler(socket);
            });
        }

    }

    // 编写一个handler方法，和客户端通讯
    public static void handler(Socket socket){
        try{
            byte[] bytes = new byte[1024];
            // 通过socket 获取输入流
            InputStream inputStream = socket.getInputStream();

            // 循环读取客户发送的数据
            while(true){

                // 连接建立之后，如果当前线程暂时没有数据可读，
                // 则线程会阻塞在read操作上
                int read = inputStream.read(bytes);
                if (read != -1){
                    System.out.println(new String(bytes, 0, read)); //输出客户端发送的数据
                } else {
                    break;
                }
            }


        }catch(Exception e){
            e.printStackTrace();
        }finally{
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
