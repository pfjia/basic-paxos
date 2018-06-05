package netty.handler;

import com.jfinal.kit.LogKit;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author pfjia
 * @since 2018/6/4 19:52
 */
public class LogInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LogKit.info("Inbound:" + msg);
        super.channelRead(ctx, msg);
    }
}
