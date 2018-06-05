package role;

import runtime.Cluster;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author pfjia
 * @since 2018/5/30 15:36
 */
public class Node implements Serializable {
    private String name;
    private InetSocketAddress address;
    private Set<Role> roleSet = new HashSet<>();
    private Cluster cluster;

    public Node(int port) {
        this("localhost", port);
    }

    public Node(String host, int port) {
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

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
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

    public void init() {
        for (Role role : roleSet) {
            role.init();
        }
    }

    public void start() {
        for (Role role : roleSet) {
            role.start();
        }
    }
}
