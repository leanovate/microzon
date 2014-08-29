class consul::install {
    exec { "download consul":
        command => "/usr/bin/wget https://dl.bintray.com/mitchellh/consul/0.3.1_linux_amd64.zip",
        cwd => "/tmp",
        creates => "/tmp/0.3.1_linux_amd64.zip"
    }

    exec { "download consul ui":
        command => "/usr/bin/wget https://dl.bintray.com/mitchellh/consul/0.3.1_web_ui.zip",
        cwd => "/tmp",
        creates => "/tmp/0.3.1_web_ui.zip"
    }

    file { ["/var/lib/consul", "/usr/share/consul", "/etc/consul", "/etc/consul/conf.d"]:
        ensure => directory,
        owner => root,
        group => root,
        mode  => 755
    }

    exec { "unpack consul":
        command => "/usr/bin/unzip /tmp/0.3.1_linux_amd64.zip",        
        cwd => "/usr/local/bin",
        creates => "/usr/local/bin/consul",
        subscribe => Exec["download consul"],
        require => Package["unzip"],
    }
}