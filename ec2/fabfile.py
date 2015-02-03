
import boto.ec2
import os
import os.path
import tempfile

from fabric.api import env, roles, run, put, sudo
from fabric.context_managers import cd
from fabric.contrib.files import append, exists, contains
from fabric.api import settings

region = os.getenv('AWS_DEFAULT_REGION', 'eu-central-1')
conn = boto.ec2.connect_to_region(region)

instances = conn.get_only_instances(filters={'tag:Environment': 'Microzon', 'tag-key':'Name', 'tag-key':'Kind', 'instance-state-name':'running'})

env.user = "ubuntu"
env.key_filename = os.path.expanduser("~/.ssh/awskey.pem")

for instance in instances:
	if not instance.tags['Kind'] in env.roledefs:
		env.roledefs[instance.tags['Kind']] = []
	env.roledefs[instance.tags['Kind']].append(instance.public_dns_name)

def launch_instances():
	instances = {
		'puppet': ['puppetmaster'],
		'consul': ['consul1', 'consul2', 'consul3'],
		'log': ['log'],
		'mysql': ['mysql'],
		'mongo': ['mongo'],
		'zipkin': ['zipkin'],
		'customer': ['customer1', 'customer2'],
		'cart': ['cart1', 'cart2'],
		'product': ['product1', 'product2'],
		'billing': ['billing1', 'billing2'],
		'web': ['web1', 'web2']
	}
	for kind in instances.keys():
		names = instances[kind]
		reservation = conn.run_instances('ami-b83c0aa5', min_count=len(names), max_count=len(names), 
		                                 key_name="awskey", instance_type="t2.micro", security_groups=['default', 'allowssh', 'allowhttp'])
		for idx in range(0, len(reservation.instances)):
			instance = reservation.instances[idx]
			conn.create_tags([instance.id], {"Name": names[idx], "Kind": kind, "Environment": "Microzon"})

def list_hosts():
	for instance in instances:
		print instance.tags['Name'].ljust(20) + instance.ip_address

@roles(env.roledefs.keys())
def host_names():
    run('hostname')

@roles("puppet")
def install_puppetmaster():
	install_puppetbase()
	sudo("apt-get install -y puppetmaster-passenger")
	sudo("apt-get install -y git")
	sudo("mkdir -p /opt")
	if not contains("/etc/puppet/puppet.conf", "manifest =", use_sudo=True):
		append("/etc/puppet/puppet.conf", "manifest = $confdir/environments/$environment/manifests/site.pp", use_sudo=True)
	if not contains("/etc/puppet/puppet.conf", "modulepath =", use_sudo=True):
		append("/etc/puppet/puppet.conf", "modulepath = $confdir/environments/$environment/modules", use_sudo=True)
	if not contains("/etc/puppet/puppet.conf", "hiera_config =", use_sudo=True):
		append("/etc/puppet/puppet.conf", "hiera_config = $confdir/hiera.yaml", use_sudo=True)
	if not exists("/etc/puppet/autosign.conf", use_sudo=True):
		append("/etc/puppet/autosign.conf", '*.%s.compute.internal' % region, use_sudo=True)
	if exists("/opt/dose"):
		update_puppetmaster()
	else:
		with cd("/opt"):
			sudo("git clone https://github.com/leanovate/dose.git")
		sudo("cp /opt/dose/vagrant/hiera/hiera.yaml /etc/puppet")
		sudo("ln -s /opt/dose/vagrant/hiera /etc/puppet/hiera")
	if not exists("/etc/puppet/environments/microzon"):
		sudo("mkdir -p /etc/puppet/environments/microzon")
		with cd("/etc/puppet/environments/microzon"):
			sudo("ln -s /opt/dose/vagrant/modules")
			sudo("ln -s /opt/dose/vagrant/manifests")

@roles("puppet")
def update_puppetmaster():
	with cd("/opt/dose"):
		sudo("git pull --rebase")
	if not exists("/etc/puppet/hiera.yaml", use_sudo=True):
		sudo("cp /opt/dose/vagrant/hiera/hiera.yaml /etc/puppet")
	if not exists("/etc/puppet/hiera", use_sudo=True):
		sudo("ln -s /opt/dose/vagrant/hiera /etc/puppet/hiera")

@roles("consul", "log", "zipkin", "mysql", "mongo", "customer", "product", "cart", "billing", "web")
def install_puppetagent():
	install_puppetbase()
	sudo("apt-get install -y puppet")
	if not contains("/etc/puppet/puppet.conf", "[agent]", use_sudo=True):
		append("/etc/puppet/puppet.conf", "[agent]", use_sudo=True)
		append("/etc/puppet/puppet.conf", "server = puppetmaster.%s.compute.internal" % region, use_sudo=True)
		append("/etc/puppet/puppet.conf", "environment = microzon", use_sudo=True)

#@roles("consul", "log", "zipkin", "mysql", "mongo", "customer", "product", "cart", "billing", "web")
@roles("billing")
def apply_puppet():
	with settings(warn_only=True):
		sudo("puppet agent --test")

def install_puppetbase():
	bootstrap()
	sudo("wget https://apt.puppetlabs.com/puppetlabs-release-precise.deb")
	sudo("dpkg -i puppetlabs-release-precise.deb")
	sudo("apt-get update")

def bootstrap():
	for instance in instances:
		if instance.public_dns_name == env.host_string:
			sudo('hostname "%s"' % instance.tags['Name'])
			sudo('echo "%s" > /etc/hostname' % instance.tags['Name'])
			break

	with tempfile.NamedTemporaryFile() as temp:
		temp.write("127.0.0.1 localhost\n")
		temp.write("::1 ip6-localhost ip6-loopback\n")
		temp.write("fe00::0 ip6-localnet\n")
		temp.write("ff00::0 ip6-mcastprefix\n")
		temp.write("ff02::1 ip6-allnodes\n")
		temp.write("ff02::2 ip6-allrouters\n")
		temp.write("ff02::3 ip6-allhosts\n")
		for instance in instances:
			temp.write("%s %s %s.%s.compute.internal\n" % (instance.private_ip_address, instance.tags['Name'], instance.tags['Name'], region))
		temp.flush()
		put(local_path=temp.name,remote_path="/etc/hosts", use_sudo=True)
	if not exists("/var/swap.1"):
		sudo("/bin/dd if=/dev/zero of=/var/swap.1 bs=1M count=2048")
		sudo("/sbin/mkswap /var/swap.1")
		sudo("/sbin/swapon /var/swap.1")
		append("/etc/fstab", "/var/swap.1 swap swap defaults 0 0", use_sudo=True)

