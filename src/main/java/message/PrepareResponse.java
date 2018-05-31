package message;

import datastructure.PaxosValue;
import datastructure.ProposalNumber;

import java.util.Objects;

/**
 * @author pfjia
 * @since 2018/5/30 14:04
 */
public class PrepareResponse extends AbstractPaxosMessage {
    private boolean promised;
    // FIXME: 2018/5/30 需要标识该response是对哪个PrepareRequest的回复吗(返回PrepareRequest的proposalNumber)?
    /**
     * acceptor accept的proposal的number,不是允诺的proposalNumber
     */
    private ProposalNumber proposalNumber;
    /**
     * acceptor accept的proposal的number
     */
    private PaxosValue paxosValue;

    public boolean isPromised() {
        return promised;
    }

    public void setPromised(boolean promised) {
        this.promised = promised;
    }

    public ProposalNumber getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(ProposalNumber proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    public PaxosValue getPaxosValue() {
        return paxosValue;
    }

    public void setPaxosValue(PaxosValue paxosValue) {
        this.paxosValue = paxosValue;
    }

    @Override
    public String messageBodyToString() {
        return String.format("Promise(%s,%d,%s)", this.promised ? "YES" : "NO", this.proposalNumber.getNumber(), this.paxosValue != null ? this.paxosValue.getValue() : "NULL");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrepareResponse that = (PrepareResponse) o;
        return promised == that.promised &&
                Objects.equals(proposalNumber, that.proposalNumber) &&
                Objects.equals(paxosValue, that.paxosValue);
    }

    @Override
    public int hashCode() {

        return Objects.hash(promised, proposalNumber, paxosValue);
    }
}
