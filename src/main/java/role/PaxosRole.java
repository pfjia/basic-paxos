package role;

import datastructure.RoleAddress;
import message.PaxosMessage;
import runtime.GlobalConfig;
import runtime.MessageReceiverThread;
import runtime.SendMessageThread;

public abstract class PaxosRole{

    protected RoleAddress myAddress;

    public void start() {
        new MessageReceiverThread(myAddress.getPortNumber(), this);
    }

    private PaxosRole() {

    }

    protected PaxosRole(RoleAddress myAddress) {
        this.myAddress = myAddress;
    }

	public void handleMessage(PaxosMessage message) {
		assert GlobalConfig.INSTANCE.getCurrentRoleAddressByRole(this).equals(message.getReceiverAddress());
	}
	
	public void sendMessage(PaxosMessage message) {
		(new SendMessageThread(message)).start();
	}

}
