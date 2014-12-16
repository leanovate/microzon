FROM untoldwind/dose-base:v1

RUN apt-get install -y nginx

ADD sources/supervisor /etc/supervisor/conf.d
RUN rm -f /etc/nginx/sites-enabled/default
ADD sources/nginx.conf /etc/nginx/nginx.conf
ADD sources/customer-nginx.conf /etc/nginx/sites-enabled/customer

RUN mkdir -p /opt/customer
ADD dist/customer-0.1.0.jar /opt/customer/customer.jar
ADD sources/application.properties /opt/customer/application.properties
