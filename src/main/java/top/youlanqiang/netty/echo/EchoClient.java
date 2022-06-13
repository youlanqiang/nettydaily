package top.youlanqiang.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

    private final String host;

    private final int port;

    public EchoClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        EchoClient client = new EchoClient("localhost", 8080);
        client.start();
    }

    public void start() throws Exception{
        final EchoClientHandler handler = new EchoClientHandler();
        EventLoopGroup group = new NioEventLoopGroup();

        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).remoteAddress(host, port)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(handler);
                        }
                    });

            ChannelFuture f = bootstrap.connect().sync();
            f.channel().closeFuture().sync();
        }finally{
            group.shutdownGracefully().sync();
        }
    }
}
