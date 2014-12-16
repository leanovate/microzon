CREATE DATABASE IF NOT EXISTS customer;

CREATE USER 'customer'@'%' IDENTIFIED BY 'customer';
GRANT ALL ON customer.* TO 'customer'@'%';

FLUSH PRIVILEGES;
