package top.youlanqiang.netty.A06sixthexample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

public class TestClientHandler extends SimpleChannelInboundHandler<MyDataInfo.Student> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.Student msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + ", " + msg);
        ctx.channel().writeAndFlush("from server:" + UUID.randomUUID());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MyDataInfo.Student student = MyDataInfo.Student.newBuilder()
                .setName("张三").setAge(20).setAddress("北京").build();

        ctx.channel().writeAndFlush(student);
    }
}
