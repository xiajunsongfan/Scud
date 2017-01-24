package com.xj.scud.clent;

import com.xj.scud.core.MessageManager;
import com.xj.scud.core.NetworkProtocol;
import com.xj.scud.core.RpcResult;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: xiajun
 * Date: 2017/01/08 12:00
 */
public class ClientInvoker implements Invoker {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientInvoker.class);

    @Override
    public void invoke(final Channel ch, final NetworkProtocol protocol, final int packageId) throws Exception {
        ChannelFuture channelFuture = ch.writeAndFlush(protocol);
        if (LOGGER.isDebugEnabled()) {
            final long startTime = System.currentTimeMillis();
            channelFuture.addListeners(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        RpcResult result = new RpcResult();
                        result.setException(future.cause());
                        MessageManager.release(packageId, result);
                    }
                    LOGGER.debug("Scud send msg packageId={} cost {}ms, channel={}, exception= {}", protocol.getSequence(), (System.currentTimeMillis() - startTime),ch.toString(), future.cause());
                }
            });
        }
    }
}
