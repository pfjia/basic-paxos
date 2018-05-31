package role;

import datastructure.PaxosValue;
import datastructure.ProposalNumber;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import message.AbstractPaxosMessage;
import message.AcceptRequest;
import message.PrepareRequest;
import message.PrepareResponse;
import netty.handler.ProposerAcceptReceivedHandler;
import netty.handler.ProposerPrepareReceivedHandler;

import java.net.SocketAddress;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author pfjia
 * @since 2018/5/30 14:05
 */
public class Proposer implements Role {
    private Node node;

    private ProposalNumber currentProposalNumber = null;

    private Map<PrepareResponse, SocketAddress> prepareResponseMap = new HashMap<>();
    private int numberOfPrepareRequest = 0;


    private int numberOfAcceptRequest = 0;
    private int numberOfAccepted = 0;
    private int numberOfNotAccepted = 0;


    public int getNumberOfAcceptRequest() {
        return numberOfAcceptRequest;
    }

    public void setNumberOfAcceptRequest(int numberOfAcceptRequest) {
        this.numberOfAcceptRequest = numberOfAcceptRequest;
    }

    public int getNumberOfAccepted() {
        return numberOfAccepted;
    }

    public void setNumberOfAccepted(int numberOfAccepted) {
        this.numberOfAccepted = numberOfAccepted;
    }

    public void incrementNumberOfAccepted() {
        numberOfAccepted++;
    }

    public void incrementNumberOfNotAccepted() {
        numberOfNotAccepted++;
    }

    public int getNumberOfNotAccepted() {
        return numberOfNotAccepted;
    }

    public void setNumberOfNotAccepted(int numberOfNotAccepted) {
        this.numberOfNotAccepted = numberOfNotAccepted;
    }

    public Map<PrepareResponse, SocketAddress> getPrepareResponseMap() {
        return prepareResponseMap;
    }

    public void setPrepareResponseMap(Map<PrepareResponse, SocketAddress> prepareResponseMap) {
        this.prepareResponseMap = prepareResponseMap;
    }


    public int getNumberOfPrepareRequest() {
        return numberOfPrepareRequest;
    }

    public void setNumberOfPrepareRequest(int numberOfPrepareRequest) {
        this.numberOfPrepareRequest = numberOfPrepareRequest;
    }


    public long getNumberOfPromise() {
        return getPrepareResponseMap()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().getProposalNumber().equals(currentProposalNumber))
                .filter(entry -> entry.getKey().isPromised())
                .count();
    }

    public long getNumberOfNotPromise() {
        return getPrepareResponseMap()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().getProposalNumber().equals(currentProposalNumber))
                .filter(entry -> !entry.getKey().isPromised())
                .count();
    }

    public ProposalNumber getCurrentProposalNumber() {
        return currentProposalNumber;
    }

    public void setCurrentProposalNumber(ProposalNumber currentProposalNumber) {
        this.currentProposalNumber = currentProposalNumber;
    }

    /**
     * @return 集群法定人数
     */
    public int getQuorum() {
        int totalNumber = getNode().getQuorum().getNodeList().size();
        return Math.floorDiv(totalNumber, 2) + 1;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void setNode(Node node) {
        this.node = node;
    }

    public void resetPrepare() {
        this.currentProposalNumber = new ProposalNumber();
        numberOfPrepareRequest = 0;
        prepareResponseMap.clear();
    }

    public void resetAccept() {
        numberOfAcceptRequest = 0;
        numberOfAccepted = 0;
        numberOfNotAccepted = 0;
    }


    @Override
    public void start() {

    }

    @Override
    public void sendMessage(AbstractPaxosMessage message) {
        sendMessage(message.getReceiverAddress(), message);
    }


    private void sendMessage(SocketAddress dest, AbstractPaxosMessage abstractPaxosMessage) {
        EventLoopGroup group = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(dest)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ProposerPrepareReceivedHandler(Proposer.this));
                            ch.pipeline().addLast(new ProposerAcceptReceivedHandler(Proposer.this));
                        }
                    });
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().writeAndFlush(abstractPaxosMessage);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void sendPrepareRequest() {
        resetPrepare();
        for (Node n : getNode().getQuorum().getAcceptorList()) {
            sendMessage(n.getAddress(), new PrepareRequest(currentProposalNumber));
        }
        numberOfPrepareRequest = getNode().getQuorum().getAcceptorList().size();
    }


    /**
     * Proposer进入accept阶段,计算proposal value,将其发送到回复prepare request的所有节点
     */
    public void sendAcceptRequest() {
        //1.计算prepare response中最大proposal number的value
        PaxosValue paxosValue = getPrepareResponseMap()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().getProposalNumber().equals(currentProposalNumber))
                .max(Comparator.comparing(o -> o.getKey().getProposalNumber()))
                .map(entry -> entry.getKey().getPaxosValue())
                .orElse(new PaxosValue("123"));
        //2.发送accept request
        getPrepareResponseMap()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().getProposalNumber().equals(currentProposalNumber))
                .forEach(
                        new Consumer<Map.Entry<PrepareResponse, SocketAddress>>() {
                            @Override
                            public void accept(Map.Entry<PrepareResponse, SocketAddress> entry) {
                                AcceptRequest request = new AcceptRequest(currentProposalNumber, paxosValue);
                                SocketAddress dest = entry.getValue();
                                sendMessage(dest, request);
                            }
                        }
                );
    }
}