package netty.handler;

import com.jfinal.kit.LogKit;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import message.AcceptResponse;
import message.AbstractPaxosMessage;
import role.Proposer;

/**
 * @author pfjia
 * @since 2018/5/30 8:44
 */
public class ProposerAcceptReceivedHandler extends ChannelInboundHandlerAdapter {
    private Proposer proposer;

    public ProposerAcceptReceivedHandler(Proposer proposer) {
        this.proposer = proposer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //1.接受prepare response
        AbstractPaxosMessage abstractPaxosMessage = (AbstractPaxosMessage) msg;
        if (abstractPaxosMessage instanceof AcceptResponse) {
            AcceptResponse response = (AcceptResponse) abstractPaxosMessage;
            if (response.getCorrespondingProposalNumber().equals(proposer.getCurrentProposalNumber())) {
                //2.修改proposer属性
                if (response.isAccept()) {
                    proposer.incrementNumberOfAccepted();
                } else {
                    proposer.incrementNumberOfNotAccepted();
                }
                //3.判断是否足够法定人数
                if (proposer.getNumberOfAccepted() >= proposer.getQuorum()) {
                    ctx.channel().eventLoop().execute(new Runnable() {
                        @Override
                        public void run() {
                            //足够法定人数,集群达到一致,结束
                            // TODO: 2018/5/30
                            LogKit.info("一致了");
                        }
                    });
                } else if (proposer.getNumberOfNotAccepted() > (proposer.getNumberOfPrepareRequest() + proposer.getQuorum())) {
                    //notAccepted的节点多于(总结点个数-quorum),提前发送下一轮prepare response
                    if (proposer.getNumberOfNotPromise() == proposer.getNumberOfPrepareRequest()) {
                        ctx.channel().eventLoop().execute(new Runnable() {
                            @Override
                            public void run() {
                                proposer.sendPrepareRequest();
                            }
                        });
                    }
                }
            }
            ctx.close();
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
