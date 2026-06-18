CREATE DATABASE IF NOT EXISTS mundial_db;
USE mundial_db;

CREATE TABLE IF NOT EXISTS usuarios (
    documento VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    edad INT NOT NULL,
    es_admin BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS partidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    grupo VARCHAR(50) NOT NULL,
    partido_nro INT NOT NULL,
    equipo_local VARCHAR(100) NOT NULL,
    equipo_visitante VARCHAR(100) NOT NULL,
    goles_local INT DEFAULT -1,
    goles_visitante INT DEFAULT -1,
    UNIQUE KEY unique_partido (grupo, partido_nro)
);

CREATE TABLE IF NOT EXISTS apuestas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_documento VARCHAR(50) NOT NULL,
    partido_id INT NOT NULL,
    goles_local INT NOT NULL,
    goles_visitante INT NOT NULL,
    FOREIGN KEY (usuario_documento) REFERENCES usuarios(documento) ON DELETE CASCADE,
    FOREIGN KEY (partido_id) REFERENCES partidos(id) ON DELETE CASCADE,
    UNIQUE KEY unique_usuario_partido (usuario_documento, partido_id)
);
