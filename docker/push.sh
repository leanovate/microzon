#!/bin/sh

docker push "untoldwind/dose:base-v3"
docker push "untoldwind/dose:consul-v1"
docker push "untoldwind/dose:consul-boot-v1"
docker push "untoldwind/dose:log-v3"
docker push "untoldwind/dose:zipkin-v3"
docker push "untoldwind/dose:mysql-v4" 
docker push "untoldwind/dose:customer-v3"
docker push "untoldwind/dose:product-v3"
docker push "untoldwind/dose:cart-v3"
docker push "untoldwind/dose:billing-v3"
docker push "untoldwind/dose:web-v4"
