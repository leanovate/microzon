FROM untoldwind/dose:base-v4

ADD sources/supervisor /etc/supervisor/conf.d
ADD sources/consul /etc/consul.d

EXPOSE 22 8300 8301 8302

CMD ["/usr/bin/supervisord", "-n", "-c", "/etc/supervisor/supervisord.conf"]
