package cn.enjoyedu.ch02.nettytest.nianbao.delimiter.customdelimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf>{

    /**
     * 读取数据时调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        String str = msg.toString(CharsetUtil.UTF_8);
        System.out.println("接收到服务器数据:"+str);
    }

    /**
     * 通道激活时调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buf = null;
        String msg = "wubo,xiatingting,yong,jie,tong,xin"+Server.DELIMITER;
        for(int i=0;i<100;i++){
            buf = Unpooled.buffer(msg.length());
            buf.writeBytes(msg.getBytes());
            ctx.writeAndFlush(buf);
        }

    }


}
