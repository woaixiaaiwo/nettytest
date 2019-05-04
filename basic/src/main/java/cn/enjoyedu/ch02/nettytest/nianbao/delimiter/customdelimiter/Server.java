package cn.enjoyedu.ch02.nettytest.nianbao.delimiter.customdelimiter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class Server {

    //自定义分隔符
    public static String DELIMITER = "@~";

    private int port;

    public Server(String host, int port){
        this.port = port;
    }

    public void start() throws InterruptedException {
        //1.定义线程组
        EventLoopGroup group = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(group,worker)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .childHandler(new ChannelInitializerImp());

            ChannelFuture future = b.bind().sync();
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    private class ChannelInitializerImp extends ChannelInitializer {

        @Override
        protected void initChannel(Channel ch) throws Exception {
            //加上自定义分隔符
            ByteBuf byteBuf = Unpooled.copiedBuffer(Server.DELIMITER.getBytes());
            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,byteBuf));
            ch.pipeline().addLast(new ServerHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Server("127.0.0.1",9999).start();
    }

}
