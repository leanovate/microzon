
class app_mysql::config {
    File {
        owner => root,
        group => root,
        mode => 644,
        ensure => file        
    }

    file { "/etc/consul.d/mysql.json":
        source => "puppet:///modules/app_mysql/mysql.json",
        notify => Service["supervisor"]
    }

    file { "/etc/mysql/my.cnf":
        source => "puppet:///modules/app_mysql/my.cnf",
        notify => Service["mysql"]
    }
}