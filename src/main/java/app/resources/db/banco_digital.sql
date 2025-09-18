CREATE DATABASE banco_digital;

USE banco_digital;

CREATE TABLE contas (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titular VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(100) NOT NULL,
    saldo DECIMAL(10,2) DEFAULT 0.0
);

INSERT INTO contas (titular, email, senha, saldo) 
VALUES  ("João", "joao@email.com", "123", 10000.0),
		("Ana", "ana@email.com", "456", 3000.0),
        ("Olivia", "olivia@email.com", "789", 5000.0),
        ("Silvia", "silvia@email.com", "3217", 11000.0)
;