package top.youlanqiang.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author youlanqiang
 * created in 2022/2/2 12:36 上午
 */
public class ChatClient {

    // 定义相关的属性
    private final String HOST = "127.0.0.1";
    private final int PORT = 8090; //服务器端口
    private Selector selector;
    private SocketChannel socketChannel;

    private String username;

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient();
        new Thread(()->{
            while(true){
                chatClient.readInfo();
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }

    // 构造器
    public ChatClient() throws IOException {
        selector = Selector.open();
        // 连接服务器
        socketChannel = SocketChannel.open(InetSocketAddress.createUnresolved(HOST, PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString();
        System.out.println(username + " is ok...");

    }

    public void sendInfo(String info){
        info = username + " 说:"+info;
        try{
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void readInfo(){
        try{
            int readChannels = selector.select(2_000);
            if(readChannels > 0){ //有可以用的通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        // 得到相关的通道
                        SocketChannel sc  = (SocketChannel) key.channel();
                        // 得到一个buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        sc.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                }
            }else{
                System.out.println("没有可用的通道");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
