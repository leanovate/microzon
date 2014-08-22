create table CART_ITEM (
    CART_ID VARCHAR(50) not null references CART(ID),
    POSITION INT not null,
    PRODUCT_ID VARCHAR(50) not null,
    PRODUCT_OPTION VARCHAR(50) not null,
    CREATED TIMESTAMP DEFAULT NOW(),
    primary key (CART_ID, POSITION)
);
