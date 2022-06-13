package top.youlanqiang.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @author youlanqiang
 * created in 2022/5/21 10:23
 */
public class HttpServer {

    public static void main(String[] args) {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .channel(NioServerSocketChannel.class)
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .childHandler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast("codec", new HttpServerCodec());
                        pipeline.addLast("aggregator", new HttpObjectAggregator(512*1024));
                        pipeline.addLast("log", new SimpleChannelInboundHandler<FullHttpRequest>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
                                System.out.println(fullHttpRequest);

                                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                                        HttpResponseStatus.OK, Unpooled.copiedBuffer("hello,netty!", CharsetUtil.UTF_8));
                                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                            }
                        });
                    }
                });
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
        future.addListener((f)->{
            if(f.isSuccess()){
                System.out.println("Channel bound");
            }else{
                System.out.println("Bind attempt failed");
                f.cause().printStackTrace();
            }
        });
    }
}
