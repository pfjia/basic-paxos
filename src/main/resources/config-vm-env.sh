sudo iptables -A INPUT -p tcp --dport 50007 -j ACCEPT
sudo iptables -A INPUT -p tcp --dport 50001 -j ACCEPT
sudo iptables -A INPUT -p tcp --dport 50002 -j ACCEPT
sudo iptables -A INPUT -p tcp --dport 50003 -j ACCEPT
sudo apt-get update
sudo apt-get install default-jre -y

