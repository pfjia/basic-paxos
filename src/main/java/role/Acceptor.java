package role;

import datastructure.PaxosValue;
import datastructure.ProposalNumber;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import message.PaxosMessage;
import netty.handler.AcceptorAcceptReceivedHandler;
import netty.handler.AcceptorPrepareReceivedHandler;

public class Acceptor implements Role {
    private Node node;
    private ProposalNumber promisedProposalNumber = null;
    private ProposalNumber acceptedProposalNumber = null;
    private PaxosValue acceptedPaxosValue = null;
    private ProposalNumber theNumberN = null;
    private boolean iPromiseIgnoreAllFutureProposalsHavingANumberLessThanN;

    public ProposalNumber getPromisedProposalNumber() {
        return promisedProposalNumber;
    }

    public void setPromisedProposalNumber(ProposalNumber promisedProposalNumber) {
        this.promisedProposalNumber = promisedProposalNumber;
    }

    public ProposalNumber getAcceptedProposalNumber() {
        return acceptedProposalNumber;
    }

    public void setAcceptedProposalNumber(ProposalNumber acceptedProposalNumber) {
        this.acceptedProposalNumber = acceptedProposalNumber;
    }

    public PaxosValue getAcceptedPaxosValue() {
        return acceptedPaxosValue;
    }

    public void setAcceptedPaxosValue(PaxosValue acceptedPaxosValue) {
        this.acceptedPaxosValue = acceptedPaxosValue;
    }


    @Override
    public void sendMessage(PaxosMessage message) {

    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public void start() {
        //1.设置Acceptor监听线程
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(getNode().getAddress().getPort())
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline().addLast(new ObjectEncoder());
                                    ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                                    ch.pipeline().addLast(new AcceptorPrepareReceivedHandler(Acceptor.this));
                                    ch.pipeline().addLast(new AcceptorAcceptReceivedHandler(Acceptor.this));
                                }
                            }
                    );
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
