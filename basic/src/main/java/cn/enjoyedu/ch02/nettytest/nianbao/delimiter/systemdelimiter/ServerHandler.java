package cn.enjoyedu.ch02.nettytest.nianbao.delimiter.systemdelimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private AtomicInteger account = new AtomicInteger(0);

    /**
     * 读取数据时调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        String str = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println("接收到客户端数据:"+str);
        account.incrementAndGet();
        ctx.writeAndFlush(Unpooled.copiedBuffer((str+ System.getProperty("line.separator")).getBytes()));
    }

    /**
     * 所有数据读取完毕后调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        System.out.println("服务器数据读取完毕,总共:"+account.get()+"次");
    }


}
