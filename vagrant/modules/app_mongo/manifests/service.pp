
class app_mongo::service {
    service { "mongod":
        ensure => running,
        enable => true,
        hasstatus => true,
        hasrestart => true
    }    
}
