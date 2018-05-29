make:
	javac -cp lib/gson-2.2.4.jar -sourcepath src src/*/*.java -d bin
config-vm-env:
	vagrant ssh bp1 -c "cd /vagrant && ./config-vm-env.sh >> /dev/null 2>&1" &
	vagrant ssh bp2 -c "cd /vagrant && ./config-vm-env.sh >> /dev/null 2>&1" &
	vagrant ssh bp3 -c "cd /vagrant && ./config-vm-env.sh >> /dev/null 2>&1" &
run:
	vagrant ssh bp1 -c "cd /vagrant && java -classpath bin:lib/gson-2.2.4.jar runtime.Main 0" &
	vagrant ssh bp2 -c "cd /vagrant && java -classpath bin:lib/gson-2.2.4.jar runtime.Main 1" &
	vagrant ssh bp3 -c "cd /vagrant && java -classpath bin:lib/gson-2.2.4.jar runtime.Main 2" &
proposal:
	ssh -i ~/.vagrant.d/insecure_private_key -p 2222 vagrant@localhost "echo $(VALUE) | nc 11.11.1.101 50007" &
kill:
	ssh -i ~/.vagrant.d/insecure_private_key -p 2222 vagrant@localhost "pkill -f 'runtime.Main'"
	ssh -i ~/.vagrant.d/insecure_private_key -p 2200 vagrant@localhost "pkill -f 'runtime.Main'"
	ssh -i ~/.vagrant.d/insecure_private_key -p 2201 vagrant@localhost "pkill -f 'runtime.Main'"
clean:
	rm -rf bin/*
