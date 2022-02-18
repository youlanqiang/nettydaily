package top.youlanqiang.netty.protobuf;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline cp = ch.pipeline();

        //protobuf 默认的4个解码器
        cp.addLast(new ProtobufVarint32FrameDecoder()) //这是针对protobuf协议的 ProtobufVarint32LengthFieldPrepender()所加的长度属性的解码器
                .addLast(new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()))
                .addLast(new ProtobufVarint32LengthFieldPrepender()) //对protobuf协议的的消息头上加上一个长度为32的整形字段，用于标志这个消息的长度。
                .addLast(new ProtobufEncoder())
                .addLast(new TestServerHandler());
    }
}
