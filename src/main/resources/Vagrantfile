# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.define :bp1 do |bp1|
    bp1.vm.provider "virtualbox" do |v|
          v.customize ["modifyvm", :id, "--name", "bp1", "--memory", "256"]
    end
    bp1.vm.box = "hashicorp/precise32"
    bp1.vm.hostname = "bp1"
    bp1.vm.network :private_network, ip: "11.11.1.101"
  end
  config.vm.define :bp2 do |bp2|
    bp2.vm.provider "virtualbox" do |v|
          v.customize ["modifyvm", :id, "--name", "bp2", "--memory", "256"]
    end
    bp2.vm.box = "hashicorp/precise32"
    bp2.vm.hostname = "bp2"
    bp2.vm.network :private_network, ip: "11.11.1.102"
  end
  config.vm.define :bp3 do |bp3|
    bp3.vm.provider "virtualbox" do |v|
          v.customize ["modifyvm", :id, "--name", "bp3", "--memory", "256"]
    end
    bp3.vm.box = "hashicorp/precise32"
    bp3.vm.hostname = "bp3"
    bp3.vm.network :private_network, ip: "11.11.1.103"
  end

  config.vm.box = "hashicorp/precise32"

end
