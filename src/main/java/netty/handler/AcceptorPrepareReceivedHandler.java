package netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import message.PaxosMessage;
import message.PrepareRequest;
import message.PrepareResponse;
import role.Acceptor;

/**
 * @author pfjia
 * @since 2018/5/30 9:15
 */
public class AcceptorPrepareReceivedHandler extends ChannelInboundHandlerAdapter {
    private Acceptor acceptor;

    public AcceptorPrepareReceivedHandler(Acceptor acceptor) {
        this.acceptor = acceptor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PaxosMessage paxosMessage = (PaxosMessage) msg;

        if (paxosMessage instanceof PrepareRequest) {
            PrepareResponse response = new PrepareResponse();
            PrepareRequest request = (PrepareRequest) paxosMessage;
            if (acceptor.getPromisedProposalNumber() == null) {
                //尚未接收任何proposalNumber
                acceptor.setPromisedProposalNumber(request.getProposalNumber());
                response.setPromised(true);
                response.setProposalNumber(acceptor.getAcceptedProposalNumber());
                response.setPaxosValue(acceptor.getAcceptedPaxosValue());
            } else {
                //判断promisedProposalNumber与请求中proposalNumber孰大孰小
                if (acceptor.getPromisedProposalNumber().compareTo(request.getProposalNumber()) <= 0) {
                    acceptor.setPromisedProposalNumber(request.getProposalNumber());
                    response.setPromised(true);
                    response.setProposalNumber(acceptor.getAcceptedProposalNumber());
                    response.setPaxosValue(acceptor.getAcceptedPaxosValue());
                } else {
                    response.setPromised(false);
                    response.setProposalNumber(acceptor.getAcceptedProposalNumber());
                    response.setPaxosValue(acceptor.getAcceptedPaxosValue());
                }
            }
            //回复
            ctx.writeAndFlush(response);
        } else {
            //不能处理则传给下一个handler
            super.channelRead(ctx, msg);
        }
    }
}
