package runtime;

import datastructure.NodeAddress;
import message.PaxosMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

public class SendMessageThread extends Thread {

    private ConnectionProtocol runOnTCPOrUDP = ConnectionProtocol.TCP_CONNECTION;

	private PaxosMessage messageToSend;
	
	public SendMessageThread(PaxosMessage message) {

        this.runOnTCPOrUDP = GlobalConfig.INSTANCE.getConnectionProtocol();
		this.messageToSend = message;
	}

    @Override
    public synchronized void run() {

        switch (this.runOnTCPOrUDP) {
            case TCP_CONNECTION:
                this.runOnTCP();
                break;
            case UDP_CONNECTION:
                this.runOnUDP();
                break;
        }
    }

	public void runOnTCP() {
		Socket socket;
		ObjectOutputStream objectOutputStream = null;
        PaxosMessage message = this.messageToSend;
        NodeAddress receiverAddress = message.getReceiverAddress();
		try {
			socket = new Socket(receiverAddress.getIp(), receiverAddress.getPort());
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject(this.messageToSend);
			objectOutputStream.flush();
            synchronized (System.out) {
                System.out.println(this.messageToSend.toSendString());
                System.out.flush();
            }
			objectOutputStream.close();
			socket.close();
		} catch (IOException e) {
			synchronized (System.out) {
                e.printStackTrace();
				System.out.println("Message send fail! --- from " + this.messageToSend.getSenderAddress() + " to " + this.messageToSend.getReceiverAddress() + " ---");
			}
		}
	}

    public void runOnUDP() {

        NodeAddress receiverAddress = this.messageToSend.getReceiverAddress();
        try {
            DatagramSocket socket = new DatagramSocket();
            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayStream);
            objectStream.writeObject(this.messageToSend);
            byte[] arr = byteArrayStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(arr, arr.length, receiverAddress.getIp(), receiverAddress.getPort());
            socket.send(packet);
            synchronized (System.out) {
                System.out.println(this.messageToSend.toSendString());
                System.out.flush();
            }
            objectStream.close();
            byteArrayStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
