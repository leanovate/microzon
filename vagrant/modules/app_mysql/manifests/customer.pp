
class app_mysql::customer {
    mysql_user { 'customer@%':
        ensure        => present,
        password_hash => mysql_password("customer"),
        require => File["/root/.my.cnf"]
    }

    mysql_database { 'customer':
        ensure => present,
        require => File["/root/.my.cnf"]
    }

    mysql_grant { 'customer@%/customer.*':
        ensure     => 'present',
        options    => ['GRANT'],
        privileges => ['ALL'],
        table      => 'customer.*',
        user       => 'customer@%',
        require    => [Mysql_user['customer@%'], Mysql_database['customer']]
    }
	
}