package datastructure;

import java.io.Serializable;

/**
 * @author pfjia
 * @since 2018/5/30 14:05
 */
public class PaxosValue implements Serializable {

    private String value = null;


    public PaxosValue(String paxosValue) {
        this.value = paxosValue;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value != null ? this.value : "RequestLeader";
    }
}
