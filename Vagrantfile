# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.

Vagrant.configure(2) do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://atlas.hashicorp.com/search.
  config.vm.box = "centos/7"

  # Disable automatic box update checking. If you disable this, then
  # boxes will only be checked for updates when the user runs
  # `vagrant box outdated`. This is not recommended.
  # config.vm.box_check_update = false

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  config.vm.network "forwarded_port", guest: 80, host: 8080

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  # config.vm.network "private_network", ip: "192.168.33.10"

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  # config.vm.network "public_network"

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  # config.vm.synced_folder "../data", "/vagrant_data"

  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:
  #
  config.vm.provider "virtualbox" do |vb|
  #   # Display the VirtualBox GUI when booting the machine
     vb.gui = true
     vb.name = "ginco dev vm"
  
     # Customize the amount of memory on the VM:
     vb.memory = "6000"
  end
  #
  # View the documentation for the provider you are using for more
  # information on available options.

  # Enable provisioning with a shell script. Additional provisioners such as
  # Puppet, Chef, Ansible, Salt, and Docker are also available. Please see the
  # documentation for more information about their specific syntax and use.
  config.vm.provision "shell", inline: <<-SHELL
	echo 'Update packages list...'
	echo "------------------------"
	yum update -y
	yum -y install epel-release
	echo 'Install XFCE...'
	echo "------------------------"
	yum -y groupinstall "X Window system"
	yum -y groups install "Xfce" 
	yum -y groups install "fonts" 
	yum -y install git java-1.8.0-openjdk-devel firefox wget htop
	echo 'PostGreSQL 9.4'
	echo "------------------------"
	rpm -Uvh http://yum.postgresql.org/9.4/redhat/rhel-7-x86_64/pgdg-centos94-9.4-3.noarch.rpm
	yum install -y postgresql94-server postgresql94-contrib pgadmin3
	/usr/pgsql-9.4/bin/postgresql94-setup initdb
	systemctl enable postgresql-9.4
	systemctl start postgresql-9.4
	systemctl set-default graphical.target
	APP_DB_USERNAME="ginco"
	APP_DB_PASSWORD="ginco"
	CREATE_USER_SQL="CREATE ROLE $APP_DB_USERNAME PASSWORD '$APP_DB_PASSWORD' CREATEDB INHERIT LOGIN;"
	sudo -u postgres psql --command="$CREATE_USER_SQL"
	ALTER_USER_SQL="ALTER USER $APP_DB_USERNAME WITH ENCRYPTED PASSWORD '$APP_DB_PASSWORD'"
	sudo -u postgres psql --command="$ALTER_USER_SQL"
	ALTER_POSTGRES_USER_SQL="ALTER USER postgres WITH ENCRYPTED PASSWORD 'postgres'"
	sudo -u postgres psql --command="$ALTER_POSTGRES_USER_SQL"
	sudo -u postgres createdb -E UTF8 --owner=$APP_DB_USERNAME $APP_DB_USERNAME
	echo "Updating postgresql connection info"
	cp /var/lib/pgsql/9.4/data/pg_hba.conf .
	chmod 666 pg_hba.conf
	sed 's/ident/md5/' < pg_hba.conf > pg_hba2.conf
	echo 'host    all             all             0.0.0.0/0               md5' >> pg_hba2.conf
	cp pg_hba2.conf /var/lib/pgsql/9.4/data/pg_hba.conf
	sudo chmod 600 /var/lib/pgsql/9.4/data/pg_hba.conf
	wget http://mirror.ibcp.fr/pub/eclipse//oomph/epp/neon/R2a/eclipse-inst-linux64.tar.gz
	reboot
  SHELL
end
