DROP TABLE CUSTOMER_ORDER;
DROP TABLE CUSTOMER;
DROP TABLE PRODUCT;


CREATE TABLE CUSTOMER (
                          uuid UUID PRIMARY KEY,
                          name VARCHAR(50) NOT NULL,
                          surname VARCHAR(50) NOT NULL,
                          birth_date DATE NOT NULL,
                          id_code VARCHAR(16) NOT NULL UNIQUE,
                          email VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE PRODUCT (
                         uuid UUID PRIMARY KEY,
                         code VARCHAR(50) NOT NULL UNIQUE,
                         name VARCHAR(50),
                         stock INT NOT NULL CHECK (stock >= 0),
                         version BIGINT NOT NULL
);

CREATE TABLE CUSTOMER_ORDER (
                       uuid UUID PRIMARY KEY,
                       customer_id_code VARCHAR(16),
                       product_code VARCHAR(50),
                       stock INT,
                       status VARCHAR(50),

                       CONSTRAINT fk_customer_id_code
                           FOREIGN KEY (customer_id_code)
                               REFERENCES customer(id_code),

                       CONSTRAINT fk_product_code
                           FOREIGN KEY (product_code)
                               REFERENCES product(code)
);