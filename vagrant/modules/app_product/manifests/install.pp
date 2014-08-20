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

    file { "/opt/product/product.jar":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "file:///vagrant/dists/product-assembly-0.1.0.jar",
    }

    package { "supervisor":
        ensure => present
    }

    package { "nginx":
        ensure => present
    }    
}
