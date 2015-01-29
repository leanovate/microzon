
class app_mysql::billing {
    mysql_user { 'billing@localhost':
        ensure        => present,
        password_hash => mysql_password("billing"),
        require => File["/root/.my.cnf"]
    }

    mysql_database { 'billing':
        ensure => present,
        require => File["/root/.my.cnf"]
    }

    mysql_grant { 'billing@localhost/cart.*':
        ensure     => 'present',
        options    => ['GRANT'],
        privileges => ['ALL'],
        table      => 'billing.*',
        user       => 'billing@localhost',
        require    => [Mysql_user['billing@localhost'], Mysql_database['billing']]
    }    	
}