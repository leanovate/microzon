#!/bin/sh

. stopAll.sh

function doIt {
	echo "$1: ${@:2}"
	${@:2}
}

doIt 'consul1' docker run -d -p 8002:80 -P --name=consul1 --hostname=consul1 untoldwind/dose:consul-v3
doIt 'consul2' docker run -d -P --name=consul2 --link consul1:consul1 --hostname=consul2 untoldwind/dose:consul-v3
doIt 'consul3' docker run -d -P --name=consul3 --link consul1:consul1 --hostname=consul3 untoldwind/dose:consul-v3

doIt 'log' docker run -d -p 8000:80 -P --name=log --hostname=log --link consul1:consul1 --link consul2:consul2 --link consul3:consul3 untoldwind/dose:log-v5
doIt 'zipkin' docker run -d -p 8001:80 -p 9410:9410 -P --name=zipkin --hostname=zipkin --link consul1:consul1 --link consul2:consul2 --link consul3:consul3 untoldwind/dose:zipkin-v3

doIt 'mysql' docker run -d -p 3306:3306 -P --name=mysql --hostname=mysql --link consul1:consul1 --link consul2:consul2 --link consul3:consul3 untoldwind/dose:mysql-v5
doIt 'mongo' docker run -d -p 27017:27017 -P --name=mongo --hostname=mongo --link consul1:consul1 --link consul2:consul2 --link consul3:consul3 untoldwind/dose:mongo-v2

# Uggly but necessary atm
sleep 5

doIt 'customer' docker run -d -P --name=customer --hostname=customer --link consul1:consul1 --link consul2:consul2 --link consul3:consul3 untoldwind/dose:customer-v5 
doIt 'product' docker run -d -p 8090:80 -P --name=product --hostname=product --link consul1:consul1 --link consul2:consul2 --link consul3:consul3 untoldwind/dose:product-v5
doIt 'cart' docker run -d -P --name=cart --hostname=cart --link mysql:mysql-db --link consul1:consul1 --link consul2:consul2 --link consul3:consul3 untoldwind/dose:cart-v5
doIt 'billing' docker run -d -P --name=billing --hostname=billing --link consul1:consul1 --link consul2:consul2 --link consul3:consul3 untoldwind/dose:billing-v5
doIt 'web' docker run -d -p 8080:80 -P --name=web --hostname=web --link consul1:consul1 --link consul2:consul2 --link consul3:consul3 untoldwind/dose:web-v5
