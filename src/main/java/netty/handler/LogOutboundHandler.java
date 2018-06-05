package netty.handler;

import com.jfinal.kit.LogKit;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @author pfjia
 * @since 2018/6/4 20:05
 */
public class LogOutboundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//        LogKit.info("Outbound:" + msg);
        super.write(ctx, msg, promise);
    }
}
