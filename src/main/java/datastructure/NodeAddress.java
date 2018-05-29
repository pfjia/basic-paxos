package datastructure;

import runtime.GlobalConfig;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class NodeAddress implements Serializable {

	private InetAddress ip;
	private int port;
	
	public NodeAddress() {
		// Prevent from being called
	}
	
	public NodeAddress(int port) throws UnknownHostException {
		this(InetAddress.getLocalHost(), port);
	}
	
	public NodeAddress(InetAddress ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	@Override
    public String toString() {
		return String.format("{%d, %s}", GlobalConfig.INSTANCE.whichNodeIsThisAddress(this), GlobalConfig.INSTANCE.whichRoleIsThisAddress(this).getSimpleName());
	}
	
	public InetAddress getIp() {
		return ip;
	}
	public int getPort() {
		return port;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		NodeAddress that = (NodeAddress) o;
		return port == that.port &&
				Objects.equals(ip, that.ip);
	}

	@Override
	public int hashCode() {

		return Objects.hash(ip, port);
	}
}
