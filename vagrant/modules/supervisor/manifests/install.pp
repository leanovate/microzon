
class supervisor::install {
    file { "/var/log/supervisor":
        owner => root,
        group => root,
        mode => 755,
        ensure => directory
    }

    package { "supervisor":
        ensure => present
    }	
}