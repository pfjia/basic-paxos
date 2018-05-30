package datastructure;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ProposalNumber implements Serializable, Comparable<ProposalNumber> {

    /**
     * 保证全局唯一递增的proposalNumber
     */
    private static AtomicInteger currentNumber = new AtomicInteger(0);
    private int number;

    public ProposalNumber() {
        this.number = currentNumber.getAndIncrement();
    }

    public ProposalNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProposalNumber that = (ProposalNumber) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {

        return Objects.hash(number);
    }

    @Override
    public int compareTo(ProposalNumber o) {
        return Integer.compare(this.getNumber(), o.getNumber());
    }

    @Override
    public String toString() {
        return Integer.toString(this.number);
    }
}
