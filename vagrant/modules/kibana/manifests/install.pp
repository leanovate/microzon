class kibana::install {
    package { "nginx":
        ensure => present
    }

    exec { "download kibana":
        command => "/usr/bin/wget https://download.elasticsearch.org/kibana/kibana/kibana-3.0.1.tar.gz",
        cwd => "/tmp",
        creates => "/tmp/kibana-3.0.1.tar.gz"
    }

    file { "/usr/share/kibana3":
        ensure => directory,
        owner => root,
        group => root,
        mode => 755
    }

    exec { "unpack kibana":
        command => "/bin/tar xzf /tmp/kibana-3.0.1.tar.gz --strip-components=1",
        cwd => "/usr/share/kibana3",
        creates => "/usr/share/kibana3/README.md",
        subscribe => Exec["download kibana"],
        require => File["/usr/share/kibana3"]
    }
}