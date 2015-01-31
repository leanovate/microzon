
class app_mysql::billing {
    mysql_user { 'billing@%':
        ensure        => present,
        password_hash => mysql_password("billing"),
        require => File["/root/.my.cnf"]
    }

    mysql_database { 'billing':
        ensure => present,
        require => File["/root/.my.cnf"]
    }

    mysql_grant { 'billing@%/cart.*':
        ensure     => 'present',
        options    => ['GRANT'],
        privileges => ['ALL'],
        table      => 'billing.*',
        user       => 'billing@%',
        require    => [Mysql_user['billing@%'], Mysql_database['billing']]
    }    	
}