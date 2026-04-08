INSERT INTO CUSTOMER (uuid, name, surname, birth_date, id_code, email)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'Mario', 'Rossi', DATE '1990-05-15', 'RSSMRA90E15H501Z',
        'mario.rossi@example.com'),
       ('550e8400-e29b-41d4-a716-446655440001', 'Luigi', 'Verdi', DATE '1985-10-22', 'VRDLGU85R22F205X',
        'luigi.verdi@example.com'),
       ('550e8400-e29b-41d4-a716-446655440002', 'Anna', 'Bianchi', DATE '1995-03-08', 'BNCNNA95C48D612K',
        'anna.bianchi@example.com'),
       ('550e8400-e29b-41d4-a716-446655440003', 'Giulia', 'Ferrari', DATE '2000-07-30', 'FRRGLI00L70H501P',
        'giulia.ferrari@example.com'),
       ('550e8400-e29b-41d4-a716-446655440004', 'Marco', 'Esposito', DATE '1992-12-12', 'SPSMRC92T12F839Y',
        'marco.esposito@example.com');

INSERT INTO PRODUCT (uuid, code, name, stock, version)
VALUES ('660e8400-e29b-41d4-a716-446655441230', 'PRD001', 'Laptop', 10, 0),
       ('660e8400-e29b-41d4-a716-446655441231', 'PRD002', 'Mouse', 50, 0),
       ('660e8400-e29b-41d4-a716-446655441232', 'PRD003', 'Tastiera', 30, 0),
       ('660e8400-e29b-41d4-a716-446655441233', 'PRD004', 'Monitor', 20, 0);

INSERT INTO CUSTOMER_ORDER (uuid, customer_id_code, product_code, stock, status)
VALUES ('660e8400-e29b-41d4-a716-446655444560', 'RSSMRA90E15H501Z', 'PRD001', 3, 'INSERTED'),
       ('660e8400-e29b-41d4-a716-446655444561', 'FRRGLI00L70H501P', 'PRD004', 7, 'INSERTED');
