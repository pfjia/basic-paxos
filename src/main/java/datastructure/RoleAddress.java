package datastructure;

import runtime.GlobalConfig;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class RoleAddress implements Serializable {

	private InetAddress ip;
	private int portNumber;
	
	public RoleAddress() {
		// Prevent from being called
	}
	
	public RoleAddress(int portNumber) throws UnknownHostException {
		this(InetAddress.getLocalHost(), portNumber);
	}
	
	public RoleAddress(InetAddress ip, int portNumber) {
		this.ip = ip;
		this.portNumber = portNumber;
	}
	
	@Override
    public String toString() {
		return String.format("{%d, %s}", GlobalConfig.INSTANCE.whichNodeIsThisAddress(this), GlobalConfig.INSTANCE.whichRoleIsThisAddress(this).getSimpleName());
	}
	
	public InetAddress getIp() {
		return ip;
	}
	public int getPortNumber() {
		return portNumber;
	}

	@Override
    public boolean equals(Object o) {
		RoleAddress roleAddress = (RoleAddress)o;
		if (this.getIp().equals(roleAddress.getIp()) && this.getPortNumber() == roleAddress.getPortNumber()) {
			return true;
		} else {
			return false;
		}
	}
}
