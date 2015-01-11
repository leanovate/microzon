#!/bin/sh

docker push "untoldwind/dose:base-v4"
docker push "untoldwind/dose:consul-v1"
docker push "untoldwind/dose:consul-boot-v1"
docker push "untoldwind/dose:log-v4"
docker push "untoldwind/dose:zipkin-v4"
docker push "untoldwind/dose:mysql-v4" 
docker push "untoldwind/dose:customer-v4"
docker push "untoldwind/dose:product-v4"
docker push "untoldwind/dose:cart-v4"
docker push "untoldwind/dose:billing-v4"
docker push "untoldwind/dose:web-v4"
