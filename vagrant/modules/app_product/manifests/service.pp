class app_product::service {
    service { "nginx":
        ensure => running,
        enable => true,
        hasstatus => true,
        hasrestart => true
    }    
}