
class supervisor::service {
	service { "supervisor":
        ensure => running,
        enable => true,
        hasstatus => true,
        hasrestart => true
    }
}