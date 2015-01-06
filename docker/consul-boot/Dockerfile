FROM untoldwind/dose:base-v3

RUN apt-get install -y nginx

RUN mkdir -p /var/log/application
RUN rm -f /etc/nginx/sites-enabled/default
ADD sources/nginx.conf /etc/nginx/nginx.conf
ADD sources/consul-nginx.conf /etc/nginx/sites-enabled/consul

ADD https://dl.bintray.com/mitchellh/consul/0.4.1_web_ui.zip /root/consul_web_ui.zip
RUN mkdir -p /usr/local/share/consul
WORKDIR /usr/local/share/consul
RUN unzip /root/consul_web_ui.zip
RUN rm -f /root/consul_web_ui.zip
ADD sources/supervisor /etc/supervisor/conf.d
ADD sources/consul /etc/consul.d

EXPOSE 22 8300 8301 8302 80

CMD ["/usr/bin/supervisord", "-n", "-c", "/etc/supervisor/supervisord.conf"]
