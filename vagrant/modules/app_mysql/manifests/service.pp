
class app_mysql::service {
	service { "mysql":
        ensure => running,
        enable => true,
        hasstatus => true,
        hasrestart => true
    }
}