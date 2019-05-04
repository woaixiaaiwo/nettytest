package cn.enjoyedu.ch02.nettytest.nianbao.delimiter.customdelimiter;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.net.InetSocketAddress;

public class Client {

    private int port;

    private String host;

    public Client(String host,int port){
        this.port = port;
        this.host = host;
    }

    public void start() throws InterruptedException {
        //1.定义线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializerImp());
            ChannelFuture future = b.connect().sync();
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
            ch.pipeline().addLast(new ClientHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Client("127.0.0.1",9999).start();
    }

}
