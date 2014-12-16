CREATE DATABASE IF NOT EXISTS customer;

CREATE USER 'custer'@'%' IDENTIFIED BY 'customer';
GRANT ALL ON customer.* TO 'customer'@'%';

FLUSH PRIVILEGES;
