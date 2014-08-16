class elasticsearch::install {
    package { "elasticsearch":
        ensure => "1.1.1",
    }
}