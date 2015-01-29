
class app_mysql::customer {
    mysql_user { 'customer@localhost':
        ensure        => present,
        password_hash => mysql_password("customer"),
        require => File["/root/.my.cnf"]
    }

    mysql_database { 'customer':
        ensure => present,
        require => File["/root/.my.cnf"]
    }

    mysql_grant { 'customer@localhost/customer.*':
        ensure     => 'present',
        options    => ['GRANT'],
        privileges => ['ALL'],
        table      => 'customer.*',
        user       => 'customer@localhost',
        require    => [Mysql_user['customer@localhost'], Mysql_database['customer']]
    }
	
}