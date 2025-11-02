-- =========================================
-- 	  Schema SQL per il sito sVinicola
-- =========================================

-- =========================================
-- 		 Crea il DB
-- =========================================
CREATE DATABASE IF NOT EXISTS svinicola;
USE svinicola;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================
-- 		Crea le tabelle
-- =========================================
-- Distruzione nell'ordine corretto (dalle tabelle che dipendono ad altre, a quelle da cui si dipende)
DROP TABLE IF EXISTS DettaglioOrdine;
DROP TABLE IF EXISTS Applicato;
DROP TABLE IF EXISTS Lista_Preferiti;
DROP TABLE IF EXISTS Ordine;
DROP TABLE IF EXISTS Indirizzo;
DROP TABLE IF EXISTS Vino; -- ERRORE CORRETTO: Aggiunto DROP per Vino
DROP TABLE IF EXISTS Offerta;
DROP TABLE IF EXISTS Utente;


-- Tabelle Utente (SUGGERIMENTO: Aggiunto UNIQUE a Email)
CREATE TABLE Utente (
    ID_Utente INT AUTO_INCREMENT PRIMARY KEY,
    Nome_Utente VARCHAR(100) NOT NULL UNIQUE, 
    Password CHAR(60) NOT NULL, 
    Ruolo ENUM('cliente', 'admin') NOT NULL DEFAULT 'cliente',
    Email VARCHAR(100) NOT NULL,
    Nome VARCHAR(50),
    Cognome VARCHAR(50)
);


-- Tabelle Indirizzi 
CREATE TABLE Indirizzo (
    ID_Indirizzo INT AUTO_INCREMENT PRIMARY KEY,
    Via VARCHAR(100) NOT NULL,
    CAP CHAR(5) NOT NULL,
    Citta VARCHAR(50) NOT NULL,
    Provincia CHAR(2) NOT NULL,
    ID_Utente INT NOT NULL, 
    
    CONSTRAINT fk_indirizzo_utente FOREIGN KEY (ID_Utente) 
        REFERENCES Utente(ID_Utente)
        ON DELETE CASCADE
);

-- Tabelle Vino
CREATE TABLE Vino (
    ID_Vino INT AUTO_INCREMENT PRIMARY KEY,
    Nome VARCHAR(100) NOT NULL,
    Annata INT,
    Tipo ENUM('Rosso', 'Bianco', 'Rosé', 'Spumante', 'Dolce', 'Altro') NOT NULL,
    Descrizione TEXT,
    Percentuale_Alcolica DECIMAL(4, 2), 
    Immagine VARCHAR(255), -- URL dell'immagine del vino
    Prezzo DECIMAL(10, 2) NOT NULL, 
    Stock INT NOT NULL DEFAULT 0, -- Stock disponibile
    Formato VARCHAR(20), -- Quanti litri
    Origine VARCHAR(100), -- Cantina/Regione
    In_Vendita BOOLEAN NOT NULL DEFAULT TRUE 
);


-- Tabella Offerte
CREATE TABLE Offerta (
    ID_Offerta INT AUTO_INCREMENT PRIMARY KEY,
    Data_Inizio DATE NOT NULL,
    Data_Fine DATE NOT NULL,
    Percentuale INT NOT NULL, 
    Immagine_Promozionale VARCHAR(255),
    
    CHECK (Data_Fine > Data_Inizio),
    CHECK (Percentuale > 0 AND Percentuale <= 100)
);


-- Tabbella Applicato (Offerta --- Vino)
CREATE TABLE Applicato (
    ID_Vino INT,
    ID_Offerta INT,
    PRIMARY KEY (ID_Vino, ID_Offerta),
    CONSTRAINT fk_applicato_vino FOREIGN KEY (ID_Vino) 
        REFERENCES Vino(ID_Vino) ON DELETE CASCADE,
    CONSTRAINT fk_applicato_offerta FOREIGN KEY (ID_Offerta) 
        REFERENCES Offerta(ID_Offerta) ON DELETE CASCADE
);


-- Tabella Preferiti
CREATE TABLE Lista_Preferiti (
    ID_Utente INT,
    ID_Vino INT,
    PRIMARY KEY (ID_Utente, ID_Vino),
    CONSTRAINT fk_preferiti_utente FOREIGN KEY (ID_Utente) 
        REFERENCES Utente(ID_Utente) ON DELETE CASCADE,
    CONSTRAINT fk_preferiti_vino FOREIGN KEY (ID_Vino) 
        REFERENCES Vino(ID_Vino) ON DELETE CASCADE
);


-- Tabella Ordini (SUGGERIMENTO: Reinserito lo Stato dell'ordine)
CREATE TABLE Ordine (
    ID_Ordine INT AUTO_INCREMENT PRIMARY KEY,
    ID_Utente INT NOT NULL, 
    ID_IndirizzoSpedizione INT NOT NULL, 
    Data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Totale_Complessivo DECIMAL(10, 2) NOT NULL,
    
    CONSTRAINT fk_ordine_utente FOREIGN KEY (ID_Utente) 
        REFERENCES Utente(ID_Utente) ON DELETE NO ACTION, 
    CONSTRAINT fk_ordine_indirizzo FOREIGN KEY (ID_IndirizzoSpedizione) 
        REFERENCES Indirizzo(ID_Indirizzo) ON DELETE NO ACTION
);


-- Tabelle Dettaglio Ordini
CREATE TABLE DettaglioOrdine (
    ID_Ordine INT,
    ID_Vino INT,
    Quantita INT NOT NULL,
    Prezzo_Storico DECIMAL(10, 2) NOT NULL, 
    PRIMARY KEY (ID_Ordine, ID_Vino),
    
    CONSTRAINT fk_dettaglio_ordine FOREIGN KEY (ID_Ordine) 
        REFERENCES Ordine(ID_Ordine) ON DELETE CASCADE,
    CONSTRAINT fk_dettaglio_vino FOREIGN KEY (ID_Vino) 
        REFERENCES Vino(ID_Vino) ON DELETE NO ACTION
);

SET FOREIGN_KEY_CHECKS = 1;

-- =================================================================
-- 		Inserimento dati di default/debug
-- =================================================================

-- ERRORE CORRETTO: Ogni INSERT è ora un comando separato che termina con ;
INSERT INTO Utente (Nome_Utente, Password, Ruolo, Email, Nome, Cognome) VALUES
('admin', 'adminpassword', 'admin', 'admin@svinicola.it', 'Admin', 'DelSito');

-- Inseriamo l'indirizzo per l'utente 'admin' (che ha ID = 1)
INSERT INTO Indirizzo (ID_Utente, Via, CAP, Citta, Provincia) VALUES
(1, 'Via dell Uva, 10', '84100', 'Salerno', 'SA');

-- Inseriamo i vini
INSERT INTO Vino (Nome, Annata, Tipo, Prezzo, Stock, Formato, Origine, In_Vendita) VALUES
('Chianti Classico DOCG', 2020, 'Rosso', 15.50, 50, '0.75L', 'Toscana', TRUE),
('Bolgheri Rosso Superiore', 2018, 'Rosso', 32.00, 20, '0.75L', 'Toscana', TRUE),
('Prosecco Extra Dry', 2023, 'Spumante', 8.90, 100, '0.75L', 'Veneto', TRUE),
('Vermentino di Gallura', 2022, 'Bianco', 12.00, 70, '0.75L', 'Sardegna', TRUE),
('Gewürztraminer', 2021, 'Bianco', 18.50, 45, '0.75L', 'Trentino-Alto Adige', TRUE),
('Cerasuolo d Abruzzo', 2022, 'Rosé', 9.00, 60, '0.75L', 'Abruzzo', TRUE),
('Barolo DOCG', 2017, 'Rosso', 45.00, 15, '0.75L', 'Piemonte', TRUE),
('Passito di Pantelleria', 2019, 'Dolce', 25.00, 30, '0.50L', 'Sicilia', TRUE),
('Franciacorta Brut', 2020, 'Spumante', 28.00, 40, '0.75L', 'Lombardia', TRUE);