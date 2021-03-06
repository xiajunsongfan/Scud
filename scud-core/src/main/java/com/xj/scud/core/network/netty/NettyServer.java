package com.xj.scud.core.network.netty;

import com.xj.scud.server.ServerConfig;
import com.xj.scud.server.ServerManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * Author: xiajun
 * Date: 2017/01/02 12:32
 */
public class NettyServer {
    private static NioEventLoopGroup bossGroup;
    private static NioEventLoopGroup workerGroup;
    private static ChannelFuture future;

    public static void start(ServerConfig config, ServerManager manager) {
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("netty-server-boss"));
        workerGroup = new NioEventLoopGroup(config.getNettyWorkPooleSize(), new DefaultThreadFactory("netty-server-work", true));
        ServerBootstrap boot = new ServerBootstrap();
        boot.group(bossGroup, workerGroup)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeout())
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 1024, 65536))
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        boot.childHandler(new NettyServerInitializer(manager));
        boot.channel(NioServerSocketChannel.class);
        future = boot.bind(config.getPort()).syncUninterruptibly();
        System.out.println("Server start sucess.");
        System.out.println("********************************_**********");
        System.out.println("*      ___    ___   _   _    __| |        *");
        System.out.println("*     /*__|  /*__| |*| |*|  /*_`*|        *");
        System.out.println("*     \\__*\\ |*(__  |*|_|*| |*(_|*|        *");
        System.out.println("*     |___/  \\___|  \\__,_|  \\__,_|v(1.0.0)*");
        System.out.println("*******************************************");
    }

    public static void stop() {
        future.channel().close().syncUninterruptibly();
        bossGroup.shutdownGracefully().syncUninterruptibly();
        workerGroup.shutdownGracefully().syncUninterruptibly();
    }
}
