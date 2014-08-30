class app_product::install {

    package { ["mongodb-org",  "mongodb-org-server", "mongodb-org-shell", "mongodb-org-mongos", "mongodb-org-tools"]:
        ensure => "2.6.1",
    }

    package { ["python-gridfs"]:
        ensure => present
    }

    file { "/opt/product":
        owner => root,
        group => root,
        mode => 755,
        ensure => directory
    }

    exec { "download product dist":
        command => "/usr/bin/curl -o /tmp/product.jar ${app_product::dist_url}",
    }

    file { "/opt/product/product.jar":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "file:///tmp/product.jar",
        require => Exec["download product dist"]
    }

    package { "supervisor":
        ensure => present
    }

    package { "nginx":
        ensure => present
    }    
}
