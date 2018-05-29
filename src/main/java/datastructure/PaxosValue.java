package datastructure;

import java.io.Serializable;

public class PaxosValue implements Serializable {

    private String paxosString = null;

    private PaxosValue() {

    }

    public PaxosValue(String paxosValue) {
        this.paxosString = paxosValue;
    }

    public String getValue() {
        return this.paxosString;
    }

    @Override
    public String toString() {
        return this.paxosString != null ? this.paxosString : "RequestLeader";
    }
}
