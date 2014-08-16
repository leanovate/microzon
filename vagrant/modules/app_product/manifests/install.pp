class app_product::install {

    package { ["mongodb-org",  "mongodb-org-server", "mongodb-org-shell", "mongodb-org-mongos", "mongodb-org-tools"]:
        ensure => "2.6.1",
    }
}
