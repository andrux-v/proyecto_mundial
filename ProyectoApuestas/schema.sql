CREATE DATABASE IF NOT EXISTS mundial_db;
USE mundial_db;

CREATE TABLE IF NOT EXISTS usuarios (
    documento BIGINT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    edad INT NOT NULL,
    es_admin BOOLEAN DEFAULT FALSE,
    contrasena VARCHAR(255) NOT NULL
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
    usuario_documento BIGINT NOT NULL,
    partido_id INT NOT NULL,
    goles_local INT NOT NULL,
    goles_visitante INT NOT NULL,
    FOREIGN KEY (usuario_documento) REFERENCES usuarios(documento) ON DELETE CASCADE,
    FOREIGN KEY (partido_id) REFERENCES partidos(id) ON DELETE CASCADE,
    UNIQUE KEY unique_usuario_partido (usuario_documento, partido_id)
);

CREATE TABLE IF NOT EXISTS historial_apuestas (
    id_historial INT AUTO_INCREMENT PRIMARY KEY,
    usuario_documento BIGINT NOT NULL,
    partido_id INT NOT NULL,
    goles_local INT NOT NULL,
    goles_visitante INT NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    tipo_accion VARCHAR(50) NOT NULL
);

DELIMITER $$

CREATE TRIGGER trigger_nueva_apuesta AFTER INSERT ON apuestas
FOR EACH ROW
BEGIN
    INSERT INTO historial_apuestas (usuario_documento, partido_id, goles_local, goles_visitante, tipo_accion)
    VALUES (NEW.usuario_documento, NEW.partido_id, NEW.goles_local, NEW.goles_visitante, 'NUEVA PREDICCIÓN');
END$$

CREATE TRIGGER trigger_actualiza_apuesta AFTER UPDATE ON apuestas
FOR EACH ROW
BEGIN
    IF OLD.goles_local != NEW.goles_local OR OLD.goles_visitante != NEW.goles_visitante THEN
        UPDATE historial_apuestas 
        SET goles_local = NEW.goles_local,
            goles_visitante = NEW.goles_visitante,
            fecha_registro = CURRENT_TIMESTAMP,
            tipo_accion = 'PREDICCIÓN MODIFICADA'
        WHERE usuario_documento = NEW.usuario_documento AND partido_id = NEW.partido_id;
    END IF;
END$$

DELIMITER ;

