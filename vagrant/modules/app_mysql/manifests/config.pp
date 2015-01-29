
class app_mysql::config {
    file { "/etc/consul.d/mysql.json":
        source => "puppet:///modules/app_mysql/mysql.json",
        notify => Service["supervisor"]
    }	
}