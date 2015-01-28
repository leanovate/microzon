
class consul_agent::install {
	exec { "download consul":
        command => "/usr/bin/wget https://dl.bintray.com/mitchellh/consul/0.4.1_linux_amd64.zip",
        cwd => "/tmp",
        creates => "/tmp/0.4.1_linux_amd64.zip"
    }

    exec { "download consul template":
    	command => "/usr/bin/wget https://github.com/hashicorp/consul-template/releases/download/v0.6.0/consul-template_0.6.0_linux_amd64.tar.gz",
    	cwd => "/tmp",
    	creates => "/tmp/consul-template_0.6.0_linux_amd64.tar.gz"
    }

    file { ["/var/consul", "/etc/consul", "/etc/consul.d", "/etc/consul-templates"]:
        ensure => directory,
        owner => root,
        group => root,
        mode  => 755
    }

    exec { "unpack consul":
        command => "/usr/bin/unzip /tmp/0.4.1_linux_amd64.zip",        
        cwd => "/usr/local/bin",
        creates => "/usr/local/bin/consul",
        subscribe => Exec["download consul"],
        require => Package["unzip"],
    }

    exec { "unpack consul template":
    	command => "/bin/tar -x -z --strip-components=1 -f /tmp/consul-template_0.6.0_linux_amd64.tar.gz",
        cwd => "/usr/local/bin",
        creates => "/usr/local/bin/consul-template",
        subscribe => Exec["download consul template"],
    }
}