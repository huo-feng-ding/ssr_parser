package com.zkdcloud.shadowsocks.client.socks5;

import com.zkdcloud.shadowsocks.client.socks5.channelHandler.inbound.CryptInitInHandler;
import com.zkdcloud.shadowsocks.client.socks5.channelHandler.inbound.Socks5ServerDoorHandler;
import com.zkdcloud.shadowsocks.client.socks5.config.ClientConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.bootstrap.ServerBootstrapConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ClientDetecter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientDetecter.class);
    private ServerBootstrap clientBootstrap = new ServerBootstrap();
    private Socks5ServerDoorHandler socks5ServerDoorHandler = new Socks5ServerDoorHandler();
    private ChannelFuture channelFuture;
    
    public ClientDetecter() throws InterruptedException {
        channelFuture = startupTCP();
    }
    
    private ChannelFuture startupTCP() throws InterruptedException {
        clientBootstrap.group(new NioEventLoopGroup(1), new NioEventLoopGroup(1))
                       .childOption(ChannelOption.SO_KEEPALIVE, true)
                       .channel(NioServerSocketChannel.class)
                       .childHandler(new ChannelInitializer<Channel>() {
                           @Override
                           protected void initChannel(Channel ch) throws Exception {
                               ch.pipeline()
                                 .addLast("idle", new IdleStateHandler(20, 20, 0, TimeUnit.MINUTES))
                                 .addLast("crypt-init", new CryptInitInHandler())
                                 .addLast("socks5-door", socks5ServerDoorHandler);
                           }
                       });
        int port = ClientConfig.clientConfig.getLocal_port();
        return clientBootstrap.bind(port).sync();
    }
    
    public void shutdown() {
        try {
            socks5ServerDoorHandler.closeChannel();
            channelFuture.channel().close();
            ServerBootstrapConfig config = clientBootstrap.config();
            config.childGroup().shutdownGracefully();
            config.group().shutdownGracefully();
        } catch (Exception e) {
            LOGGER.warn("client shutdown error:{}", e.getMessage());
        }
    }
}
