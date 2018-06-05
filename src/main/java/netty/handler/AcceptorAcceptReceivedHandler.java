package netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import message.AcceptRequest;
import message.AcceptResponse;
import message.AbstractPaxosMessage;
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
        AbstractPaxosMessage abstractPaxosMessage = (AbstractPaxosMessage) msg;
        if (abstractPaxosMessage instanceof AcceptRequest) {
            AcceptRequest request = (AcceptRequest) abstractPaxosMessage;
            AcceptResponse response;
            if (request.getProposalNumber().equals(acceptor.getPromisedProposalNumber())) {
                response=new AcceptResponse(request.getReceiver(),request.getSender(),request.getProposalNumber(),true);
                acceptor.setAcceptedProposalNumber(request.getProposalNumber());
                acceptor.setAcceptedPaxosValue(request.getValue());
            }else {
                response=new AcceptResponse(request.getReceiver(),request.getSender(),request.getProposalNumber(),false);
            }
            ctx.writeAndFlush(response);
        } else {
            super.channelRead(ctx, msg);
        }

    }
}
