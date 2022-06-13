package top.youlanqiang.netty.https;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @author youlanqiang
 * created in 2022/3/7 4:02 下午
 * 实现HTTPS请求
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {

    private final SslContext context;

    private final boolean startTls;

    public SslChannelInitializer(SslContext context, boolean startTls){
        this.context = context;
        this.startTls = startTls; // 设置为true，第一个写入的消息不会被加密（客户端应该设置为true）
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        // 每一个SslHandler实例都要使用Channel的ByteBufAllocator从SslContext获取一个新的SSLEngine
        SSLEngine engine = context.newEngine(channel.alloc());
        // 将SslHandler作为第一个ChannelHandler添加到Pipeline中
        channel.pipeline().addFirst("ssl", new SslHandler(engine, startTls));

    }
}
