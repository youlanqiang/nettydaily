package top.youlanqiang.nio.chat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author youlanqiang
 * created in 2022/2/2 12:43 上午
 */
public class ChatServer {

    private ServerSocketChannel serverChannel;

    private Selector selector;

    public ChatServer(){
        try{
            // 1. 得到选择器
            selector = Selector.open();
            // 创建ServerChannel
            serverChannel = ServerSocketChannel.open();
            // 绑定端口
            serverChannel.bind(InetSocketAddress.createUnresolved("localhost", 8090));
            // 设置非阻塞
            serverChannel.configureBlocking(false);
            // 注册selector
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void listen(){

        try {
            // 事件处理
            while (true) {
                int count = selector.select(2_000);
                if (count > 0) { // 有事件处理
                    Iterator<SelectionKey> iterator
                            = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();

                        if(key.isAcceptable()){
                            SocketChannel sc = serverChannel.accept();
                            //将该sc 注册到selector
                            sc.register(selector, SelectionKey.OP_READ);
                            // 提示
                            System.out.println(sc.getRemoteAddress() + "上线");

                        }else if(key.isReadable()){ //通道发送read事件


                        }
                        iterator.remove();
                    }
                }else{
                    System.out.println("等待...");
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{

        }
    }


    private void readData(SelectionKey key){
        SocketChannel channel = null;
        try {
            // 得到channel
            channel = (SocketChannel) key.channel();
            // 创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            if(count > 0){ //根据count的值做处理
                String msg = new String(buffer.array());
                System.out.println("form 客户端:" + msg);
                //向其他客户端转发消息
                sendInfoToOtherClients(msg, channel);
            }

        }catch(IOException e){
            try{
                System.out.println(channel.getRemoteAddress() + "离线了");
                //取消注册
                key.cancel();
                // 关闭
                channel.close();
            }catch (IOException e2){
                e2.printStackTrace();
            }
        }

    }

    //转发消息给其他客户
    public void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器消息群发中。。。");
        for (SelectionKey key : selector.keys()) {
            //通过key 取出对应的SocketChannel
            Channel targetChannel = key.channel();

            if(targetChannel instanceof SocketChannel && targetChannel != self){
                //转型
                SocketChannel dest = (SocketChannel) targetChannel;
                // 将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // 将buffer的数据写入到通道
                dest.write(buffer);
            }
        }
    }
}
