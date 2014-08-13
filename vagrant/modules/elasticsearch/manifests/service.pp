class elasticsearch::service {
    service { "elasticsearch":
        enable => true,
        ensure => running,
        hasstatus => true,
        hasrestart => true
    }
}