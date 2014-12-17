
FROM untoldwind/dose:base-v1

RUN apt-get install -y nginx

ADD sources/supervisor /etc/supervisor/conf.d
RUN rm -f /etc/nginx/sites-enabled/default
ADD sources/nginx.conf /etc/nginx/nginx.conf
ADD sources/cart-nginx.conf /etc/nginx/sites-enabled/cart

RUN mkdir -p /opt/cart
ADD dist/cart-assembly-0.1.0.jar /opt/cart/cart.jar

EXPOSE 22 80

CMD ["/usr/bin/supervisord", "-n", "-c", "/etc/supervisor/supervisord.conf"]
