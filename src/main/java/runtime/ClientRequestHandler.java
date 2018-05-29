package runtime;

import datastructure.PaxosValue;
import role.Proposer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientRequestHandler implements Runnable {
	private Proposer proposer;

	private ClientRequestHandler() {
		
	}
	
	public ClientRequestHandler(Proposer proposer) {
		this.proposer = proposer;
	}
	
	@Override
    public void run() {
		ServerSocket serverSocket = null;
		Socket acceptedSocket = null;
		BufferedReader bufferReader = null;
		String receivedString;

		try {
			serverSocket = new ServerSocket(GlobalConfig.INSTANCE.getClientListenPortNumber());

			while (true) {
				acceptedSocket = serverSocket.accept();
				bufferReader = new BufferedReader(new InputStreamReader(
						acceptedSocket.getInputStream()));
				receivedString = bufferReader.readLine();
				proposer.initiatePaxos(new PaxosValue(receivedString));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (Exception ex) {
			}
		}
	}

}
