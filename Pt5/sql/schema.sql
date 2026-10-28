CREATE DATABASE IF NOT EXISTS botiga CHARACTER SET utf8mb4;
USE botiga;

CREATE TABLE IF NOT EXISTS Clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    correu VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS Productes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    preu DECIMAL(10,2) NOT NULL CHECK (preu >= 0),
    estoc INT NOT NULL CHECK (estoc >= 0)
);

CREATE TABLE IF NOT EXISTS Comandes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (client_id) REFERENCES Clients(id)
);

CREATE TABLE IF NOT EXISTS LiniesComanda (
    id INT AUTO_INCREMENT PRIMARY KEY,
    comanda_id INT NOT NULL,
    producte_id INT NOT NULL,
    quantitat INT NOT NULL,
    preuUnitari DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (comanda_id) REFERENCES Comandes(id),
    FOREIGN KEY (producte_id) REFERENCES Productes(id)
);

CREATE TABLE IF NOT EXISTS Descomptes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producte_id INT NOT NULL,
    tipus ENUM('%','€') NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (producte_id) REFERENCES Productes(id)
);
