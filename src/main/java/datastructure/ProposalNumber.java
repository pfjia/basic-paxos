package datastructure;

import runtime.GlobalConfig;

import java.io.Serializable;

public class ProposalNumber implements Serializable, Comparable<ProposalNumber>{

	public static int currentNumber = GlobalConfig.INSTANCE.getCurrentNodeNumber();
	private int numberValue;

	public ProposalNumber() {
		this.numberValue = currentNumber + GlobalConfig.INSTANCE.getMaximumNodeNumber();
		currentNumber = this.numberValue;
	}

    public ProposalNumber(int numberValue) {
        this.numberValue = numberValue;
        currentNumber = this.numberValue;
    }

	public int getNumberValue() {
		return numberValue;
	}
	
	public boolean equals(ProposalNumber arg0) {
		if (this.getNumberValue() == arg0.getNumberValue()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int compareTo(ProposalNumber arg0) {
        ProposalNumber proposalNumber = (ProposalNumber) arg0;
        if (this.getNumberValue() == proposalNumber.getNumberValue()) {
            return 0;
        } else if (this.getNumberValue() < proposalNumber.getNumberValue()) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return Integer.toString(this.numberValue);
    }
}
