package netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import message.AcceptRequest;
import message.AcceptResponse;
import message.PaxosMessage;
import role.Acceptor;

/**
 * @author pfjia
 * @since 2018/5/30 15:36
 */
public class AcceptorAcceptReceivedHandler extends ChannelInboundHandlerAdapter {
    private Acceptor acceptor;

    public AcceptorAcceptReceivedHandler(Acceptor acceptor) {
        this.acceptor = acceptor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PaxosMessage paxosMessage = (PaxosMessage) msg;
        if (paxosMessage instanceof AcceptRequest) {
            AcceptRequest request = (AcceptRequest) paxosMessage;
            AcceptResponse response;
            if (request.getProposalNumber().equals(acceptor.getAcceptedProposalNumber())) {
                response=new AcceptResponse(request.getProposalNumber(),true);
                acceptor.setAcceptedProposalNumber(request.getProposalNumber());
                acceptor.setAcceptedPaxosValue(request.getValue());
            }else {
                response=new AcceptResponse(request.getProposalNumber(),false);
            }
            ctx.writeAndFlush(response);
        } else {
            super.channelRead(ctx, msg);
        }

    }
}