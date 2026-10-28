USE botiga;
-- Executar una sola vegada sobre una base de dades acabada de crear.
INSERT INTO Clients (nom, correu) VALUES
    ('Anna', 'anna@example.com'), ('Pere', 'pere@example.com');
INSERT INTO Productes (nom, preu, estoc) VALUES
    ('Ratolí', 19.99, 20), ('Teclat', 29.99, 15), ('Pantalla', 120.00, 10);
INSERT INTO Descomptes (producte_id, tipus, valor)
    SELECT id, '%', 10 FROM Productes WHERE nom = 'Ratolí';
INSERT INTO Descomptes (producte_id, tipus, valor)
    SELECT id, '€', 20 FROM Productes WHERE nom = 'Pantalla';
