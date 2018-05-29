basic-paxos
===========

A Java implementation of basic Paxos algorithm. This is extracted from my group assignment of Distributed Algorithms, the other teammates' work has been removed or rewritten.

1. Install Vagrant
2. cd to current directory.
3. > vagrant up
4. > make config-vm-env
5. Wait for configuring iptables and installing JRE on VM.
6. > make kill (to kill previous running instances)
7. > make run
8. > make proposal VALUE=<PaxosValue> (e.g. make proposal VALUE="hello")
9. -> 8.
