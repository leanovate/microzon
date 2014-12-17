CREATE DATABASE IF NOT EXISTS customer;
CREATE DATABASE IF NOT EXISTS billing;
CREATE DATABASE IF NOT EXISTS cart;

CREATE USER 'customer'@'%' IDENTIFIED BY 'customer';
GRANT ALL ON customer.* TO 'customer'@'%';

CREATE USER 'billing'@'%' IDENTIFIED BY 'billing';
GRANT ALL ON billing.* TO 'billing'@'%';

CREATE USER 'cart'@'%' IDENTIFIED BY 'cart';
GRANT ALL ON cart.* TO 'cart'@'%';

FLUSH PRIVILEGES;
