package role;

import message.AbstractPaxosMessage;
import runtime.Quorum;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author pfjia
 * @since 2018/5/30 15:36
 */
public class Node {
    private String name;
    private InetSocketAddress address;
    private Set<Role> roleSet = new HashSet<>();
    private Quorum quorum;

    public Node(int port) throws UnknownHostException {
        this.address = new InetSocketAddress(port);
    }

    public Node(String host, int port) throws UnknownHostException {
        this.address = new InetSocketAddress(host, port);
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public String getName() {
        if (Objects.nonNull(name)) {
            return name;
        }
        return address.toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public Quorum getQuorum() {
        return quorum;
    }

    public void setQuorum(Quorum quorum) {
        this.quorum = quorum;
    }

    public void setRole(Role role) {
        roleSet.add(role);
        role.setNode(this);
    }

    public Proposer getProposer() {
        return roleSet.stream().filter(role -> role instanceof Proposer)
                .map(role -> (Proposer) role)
                .findAny().orElse(null);
    }

    public boolean hasRole(Class<? extends Role> roleClass) {
        return roleSet.stream().anyMatch(roleClass::isInstance);
    }

    public void start() {
        for (Role role : roleSet) {
            role.start();
        }
    }
}
