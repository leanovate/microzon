class app_web::service {
    service { "supervisor":
        ensure => running,
        enable => true,
        hasstatus => true,
        hasrestart => true
    }

    service { "nginx":
        ensure => running,
        enable => true,
        hasstatus => true,
        hasrestart => true
    }
}