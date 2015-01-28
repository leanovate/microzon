class consul::install {
    exec { "download consul":
        command => "/usr/bin/wget https://dl.bintray.com/mitchellh/consul/0.4.1_linux_amd64.zip",
        cwd => "/tmp",
        creates => "/tmp/0.4.1_linux_amd64.zip"
    }

    exec { "download consul ui":
        command => "/usr/bin/wget https://dl.bintray.com/mitchellh/consul/0.4.1_web_ui.zip",
        cwd => "/tmp",
        creates => "/tmp/0.4.1_web_ui.zip"
    }

    file { ["/var/lib/consul", "/usr/local/share/consul", "/etc/consul", "/etc/consul/conf.d"]:
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

    exec { "unpack consul web ui":
        command => "/usr/bin/unzip /tmp/0.4.1_web_ui.zip",        
        cwd => "/usr/local/share/consul",
        creates => "/usr/local/share/web",
        subscribe => Exec["download consul ui"],
        require => Package["unzip"],
    }
}