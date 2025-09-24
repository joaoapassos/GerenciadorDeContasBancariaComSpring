CREATE DATABASE banco_digital;

USE banco_digital;

CREATE TABLE contas (
	numero BIGINT PRIMARY KEY AUTO_INCREMENT,
    titular VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(100) NOT NULL,
    saldo DECIMAL(10,2) DEFAULT 0.0
);

INSERT INTO contas (titular, email, senha, saldo) 
VALUES  ("João", "joao@email.com", "123", 10000.0),
		("Ana", "ana@email.com", "123", 3000.0),
        ("Olivia", "olivia@email.com", "123", 5000.0),
        ("Silvia", "silvia@email.com", "123", 11000.0)
;

CREATE TABLE movimentacoes (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_conta BIGINT NOT NULL,
    tipo VARCHAR(20) NOT NULL, -- saque, deposito, transferencia
    valor DOUBLE NOT NULL,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (numero_conta) REFERENCES contas(numero)
);