package top.youlanqiang.nio.line.client;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

/**
 * @author youlanqiang
 * created in 2022/4/21 23:19
 */
public class ChatClient {

    // 缓冲区的大小
    private final static int BUFFER_SIZE = 1024 * 8;
    // 缓冲区
    private final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

    private Selector selector;

    private SocketAddress address;


    public void connect(String host, Integer port) throws IOException {
        InetSocketAddress address = InetSocketAddress.createUnresolved(host, port);
        connect(address);
    }

    public void connect(SocketAddress address) throws IOException{
        this.address = address;
        this.initAndRegister();
    }

    private void initAndRegister()throws IOException{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        this.selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(address);
        this.work();
    }


    private void work() {
        for(;;){
            try {
                int select = selector.select();
                if(select == 0){
                    continue;
                }

                Set<SelectionKey> keys = selector.selectedKeys();
                keys.stream().iterator().forEachRemaining(x->{

                    try {
                        if (x.isConnectable()) {
                            handConnect(x);
                        }
                        if (x.isWritable()) {

                        }
                        if (x.isReadable()) {

                        }

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                });

            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private void handRead(SelectionKey key) throws Exception{
        if(!key.isValid() || !key.isReadable()){
            throw new Exception("Valid SelectionKey Error.");
        }
        SocketChannel sc = (SocketChannel)key.channel();
        

    }

    private void handConnect(SelectionKey key) throws Exception {
        if(!key.isValid() || !key.isConnectable()){
            throw new Exception("Valid SelectionKey Error.");
        }
        SocketChannel sc = (SocketChannel)key.channel();
        if(sc.isConnectionPending()){
            sc.finishConnect();
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ);
        }else{
            throw new Exception("Server Connection Error.");
        }
        key.cancel();

    }




}
