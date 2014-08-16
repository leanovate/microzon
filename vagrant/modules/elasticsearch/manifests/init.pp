
class elasticsearch {
    contain "elasticsearch::install"
    contain "elasticsearch::config"
    contain "elasticsearch::service"

    Class["elasticsearch::install"] -> 
    Class["elasticsearch::config"] ~> 
    Class["elasticsearch::service"]
}