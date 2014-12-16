
FROM untoldwind/dose:base-v1

RUN apt-get install -y nginx
RUN apt-get install -y unzip

ADD sources/supervisor /etc/supervisor/conf.d
RUN rm -f /etc/nginx/sites-enabled/default
ADD sources/nginx.conf /etc/nginx/nginx.conf
ADD sources/zipkin-nginx.conf /etc/nginx/sites-enabled/zipkin

ADD https://dl.dropboxusercontent.com/u/3815280/zipkin-collector-service.zip /root/
ADD https://dl.dropboxusercontent.com/u/3815280/zipkin-query-service.zip /root/
ADD https://dl.dropboxusercontent.com/u/3815280/zipkin-web.zip /root/

RUN mkdir -p /var/log/zipkin
RUN mkdir -p /opt/app/zipkin-collector-service
WORKDIR /opt/app/zipkin-collector-service
RUN unzip /root/zipkin-collector-service.zip
RUN mkdir -p /opt/app/zipkin-query-service
WORKDIR /opt/app/zipkin-query-service
RUN unzip /root/zipkin-query-service.zip
RUN mkdir -p /opt/app/zipkin-web
WORKDIR /opt/app/zipkin-web
RUN unzip /root/zipkin-web.zip
RUN rm -f /root/zipkin-collector-service.zip /root/zipkin-query-service.zip /root/zipkin-web.zip

EXPOSE 22 80 9410

CMD ["/usr/bin/supervisord", "-n", "-c", "/etc/supervisor/supervisord.conf"]
