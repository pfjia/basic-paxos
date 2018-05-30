package netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import message.PaxosMessage;
import message.PrepareResponse;
import role.Proposer;

/**
 * @author pfjia
 * @since 2018/5/30 13:12
 */
public class ProposerPrepareReceivedHandler extends ChannelInboundHandlerAdapter {
    private Proposer proposer;

    public ProposerPrepareReceivedHandler(Proposer proposer) {
        this.proposer = proposer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PaxosMessage paxosMessage = (PaxosMessage) msg;
        if (paxosMessage instanceof PrepareResponse) {
            PrepareResponse response = (PrepareResponse) paxosMessage;
            //1.确保是对currentProposalNumber的回应
            if (response.getProposalNumber().compareTo(proposer.getCurrentProposalNumber()) == 0) {
                //2.修改proposer属性
                if (response.isPromised()) {
                    proposer.incrementNumberOfPromise();
                } else {
                    proposer.incrementNumberOfNotPromise();
                }
                proposer.getPrepareResponseMap().put(response, ctx.channel().remoteAddress());
                //3.判断是否足够法定人数
                if (proposer.getNumberOfPromise() >= proposer.getQuorum()) {
                    ctx.channel().eventLoop().execute(new Runnable() {
                        @Override
                        public void run() {
                            //足够法定人数,向所有Acceptor发送accept request
                            proposer.sendAcceptRequest();
                        }
                    });
                } else if (proposer.getNumberOfNotPromise() > (proposer.getNumberOfPrepareRequest() + proposer.getQuorum())) {
                    //notPromise的节点多于(总结点个数-quorum),提前发送下一轮prepare response
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
            // FIXME: 2018/5/30 channel关闭后在channel.eventLoop()中执行的任务是否继续执行?
            ctx.close();
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
