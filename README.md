basic-paxos
===========

A Java implementation of basic Paxos algorithm.

basic-paxos :
Phase 1. 
(a) A proposer selects a proposal number n and sends a prepare
request with number n to a majority of acceptors.
补充:此时可以发送给all acceptors,而不是majority of acceptors.(算法中发送给所有acceptor)
(b) If an acceptor receives a prepare request with number n greater
than that of any prepare request to which it has already responded,
then it responds to the request with a promise not to accept any more
proposals numbered less than n and with the highest-numbered proposal (if any) that it has accepted.
补充:若acceptor已经接受了一个prepare request,且其number比n大,此时acceptor可以
1. 忽略该prepare request,不做任何回复
2. 回复该request,但允诺为false(个人认为此种策略更佳,因此若采用策略1,无法区分宕机和不允诺.算法中选择该策略)

Phase 2. (a) If the proposer receives a response to its prepare requests
(numbered n) from a majority of acceptors, then it sends an accept
request to each of those acceptors for a proposal numbered n with a
value v, where v is the value of the highest-numbered proposal among
the responses, or is any value if the responses reported no proposals.
补充:
若使用上述补充b1:
1. 在规定的时间内没有收到多数回复,认为此次prepare阶段失败,回到phase 1.
若使用上述补充b2:
1. 若收到允诺为true的回复>=quorum,进入phase 2.
2. 若收到允诺为false的回复>(total-quorum),此次prepare阶段失败,回到phase 1.(若收到所有回复,此时条件1,2必有一个成立,不用单独判断)
3. 若未达到条件1,2,超时后认为此次prepare阶段失败,回到phase 1.
另: 
1. 需要判断回复是否是针对currentProposalNumber
2. 发送accept request给回复了prepare request的acceptor还是所有acceptor?
个人认为此时发送给所有acceptor更佳,因为存在这样的情景:存在一个acceptor收到了prepare request,但是acceptor回复proposer时由于网络原因导致
proposer没有收到该回复,此时若发送accept request给所有acceptor,则该acceptor就会收到accept request,可以加快收敛速度.(算法中选择发送给回复了prepare request的acceptor)

(b) If an acceptor receives an accept request for a proposal numbered
n, it accepts the proposal unless it has already responded to a prepare
request having a number greater than n. 
补充:
1. 设rn(responded n)为已回复的prepare request中的最大值
(1)若n==rn,accept proposal.
(2)若n<rn,拒绝提案.
(3)若n>rn(若补充a3中发送accept request给所有acceptor则可能出现该情况)
假设如下情景:有两个proposer(p1,p2),三个acceptor(a1,a2,a3)
p1发送prepare(1)给a1,a3
p1发送accept(1,a)给a1,a2,a3,但是只有a1收到accept(1,a)
p2发送prepare(2)给a2,a3
p2发送accpet(2,b)给a1,a2,a3,但是只有a2收到accept(2,b)
=====================================此时a1收到提案(1,a),a2收到提案(2,b)
p1发送prepare(3)给a1,a3
p1发送accpet(3,a)给a1,a2,a3,但是只有a2收到accept(3,a)
p2发送prepare(4)给a2,a3
p2发送accept(4,b)给a1,a2,a3,但是只有a1收到accept(4,b)
=====================================此时a1收到提案(4,b),a2收到提案(3,a)
...(无限循环)

此时在accept阶段也有可能出现活性问题.
因此选择拒绝提案.
2. 若accept拒绝提案,是否回复?回复中是否添加额外信息(如已接受的提案)?
(简单起见,算法中选择回复但是不添加额外信息)

补充:
Phase 3:
(a)
1. 若收到接受提案的回复>=quorum,此时集群chosen a value,proposer认为集群达到一致状态,退出.
2. 若收到拒绝提案的回复>(total-quorum),此次accept阶段失败,回到phase 1.(若收到所有回复,此时条件1,2必有一个成立,不用单独判断)
3. 若未达到条件1,2,超时后认为此次accept阶段失败,回到phase 1.



Question:
1. acceptor是否可以accept多个值?
答:可以.虽然在paxos推导过程中,曾说每个acceptor最多accept一个value.但是在关于acceptor如何accept value的限制只有一条.
即P1a. An acceptor can accept a proposal numbered n iff it has not responded
 to a prepare request having a number greater than n.
 由于一旦一个value v被chosen后,此后任意proposer提出的high-numberd proposal的value都是v(P2b),所以所有accept的
 value是v的acceptor都不可能accept其他值.
 因此acceptor accept多个值的场景只有一种:acceptor accept了一个value,但该value没有被majorities accept(即没有被chosen),而后acceptor又accept了最终被
 chosen的value.
 
 2. 为什么存在多次chosen a value过程?
 答:首先需要明确什么是chosen a value,当集群中majorities accept一个相同的proposal时,该proposal拥有value v,则我们称集群chosen a value.
 假设一个集群有5个结点(a1,a2,a3,a4,a5),若a1,a2,a3 accept了proposal(1,v),此时我们称集群chosen了value,该value是v.可是集群要达到一致,即所有
 acceptor accept一个相同的value,因此paxos算法继续,若接下来a2,a3,a4 accept了proposal(2,v),此时我们仍然称集群chosen了value,该value也是v.
 
 
计划:
1. 整理代码思路
2. 修改为支持多个角色
3. 将代码进行封装
4. 使用Netty进行消息的发送和接收


