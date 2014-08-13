class kibana::service {
    service { "nginx":
        enable => true,
        ensure => running,
        hasstatus => true,
        hasrestart => true
    }
}