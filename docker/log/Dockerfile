FROM dose-base

RUN apt-get install -y elasticsearch=1.1.1
RUN apt-get install -y logstash=1.4.2-1-2c0f5a1
RUN apt-get install -y nginx

ADD https://download.elasticsearch.org/kibana/kibana/kibana-3.1.2.tar.gz /root/
ADD sources/logstash-forwarder.crt /etc/pki/server/logstash-forwarder.crt
ADD sources/logstash-forwarder.key /etc/pki/server/logstash-forwarder.key
ADD sources/01-lumberjack-input.conf /etc/logstash/conf.d/01-lumberjack-input.conf
ADD sources/10-syslog.conf /etc/logstash/conf.d/10-syslog.conf
ADD sources/11-application.conf /etc/logstash/conf.d/11-application.conf
ADD sources/12-accesslog.conf /etc/logstash/conf.d/12-accesslog.conf
ADD sources/30-lumberjack-output.conf /etc/logstash/conf.d/30-lumberjack-output.conf
ADD sources/elasticsearch.conf /etc/supervisor/conf.d/elasticsearch.conf
ADD sources/logstash.conf /etc/supervisor/conf.d/logstash.conf
RUN mkdir -p /var/run/elasticsearch

RUN mkdir -p /usr/share/kibana3
WORKDIR /usr/share/kibana3
RUN /bin/tar xzf /root/kibana-3.1.2.tar.gz --strip-components=1
RUN rm -f /etc/nginx/sites-enabled/default
ADD sources/nginx.conf /etc/nginx/nginx.conf
ADD sources/nginx-supervisor.conf /etc/supervisor/conf.d/nginx.conf
ADD sources/kibana-nginx.conf /etc/nginx/sites-enabled/kibana
ADD sources/config.js /usr/share/kibana3/config.js

EXPOSE 22 80 5000

CMD /usr/bin/supervisord -n -c /etc/supervisor/supervisord.conf
