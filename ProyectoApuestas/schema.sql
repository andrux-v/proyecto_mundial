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
    fecha_hora DATETIME NOT NULL,
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



-- ==========================================
-- SEMBRADO DE DATOS INICIALES
-- ==========================================

-- Sembrado de administrador
INSERT IGNORE INTO usuarios (documento, nombre, edad, es_admin, contrasena) VALUES (1, 'Administrador', 30, true, 'admin');

-- Sembrado de partidos
-- Grupo A
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo A', 0, 'México', 'Sudáfrica', -1, -1, '2026-06-11 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo A', 1, 'Corea del Sur', 'República Checa', -1, -1, '2026-06-11 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo A', 2, 'México', 'Corea del Sur', -1, -1, '2026-06-18 10:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo A', 3, 'Sudáfrica', 'República Checa', -1, -1, '2026-06-18 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo A', 4, 'México', 'República Checa', -1, -1, '2026-06-24 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo A', 5, 'Sudáfrica', 'Corea del Sur', -1, -1, '2026-06-24 13:00:00');

-- Grupo B
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo B', 0, 'Canadá', 'Bosnia-Herzegovina', -1, -1, '2026-06-12 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo B', 1, 'Qatar', 'Suiza', -1, -1, '2026-06-12 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo B', 2, 'Canadá', 'Qatar', -1, -1, '2026-06-18 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo B', 3, 'Bosnia-Herzegovina', 'Suiza', -1, -1, '2026-06-18 19:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo B', 4, 'Canadá', 'Suiza', -1, -1, '2026-06-24 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo B', 5, 'Bosnia-Herzegovina', 'Qatar', -1, -1, '2026-06-24 16:00:00');

-- Grupo C
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo C', 0, 'Brasil', 'Marruecos', -1, -1, '2026-06-13 10:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo C', 1, 'Haití', 'Escocia', -1, -1, '2026-06-13 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo C', 2, 'Brasil', 'Haití', -1, -1, '2026-06-19 10:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo C', 3, 'Marruecos', 'Escocia', -1, -1, '2026-06-19 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo C', 4, 'Brasil', 'Escocia', -1, -1, '2026-06-25 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo C', 5, 'Marruecos', 'Haití', -1, -1, '2026-06-25 13:00:00');

-- Grupo D
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo D', 0, 'EE. UU.', 'Paraguay', -1, -1, '2026-06-13 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo D', 1, 'Australia', 'Turquía', -1, -1, '2026-06-13 19:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo D', 2, 'EE. UU.', 'Australia', -1, -1, '2026-06-19 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo D', 3, 'Paraguay', 'Turquía', -1, -1, '2026-06-19 19:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo D', 4, 'EE. UU.', 'Turquía', -1, -1, '2026-06-25 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo D', 5, 'Paraguay', 'Australia', -1, -1, '2026-06-25 16:00:00');

-- Grupo E
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo E', 0, 'Alemania', 'Curazao', -1, -1, '2026-06-14 10:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo E', 1, 'Costa de Marfil', 'Ecuador', -1, -1, '2026-06-14 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo E', 2, 'Alemania', 'Costa de Marfil', -1, -1, '2026-06-20 10:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo E', 3, 'Curazao', 'Ecuador', -1, -1, '2026-06-20 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo E', 4, 'Alemania', 'Ecuador', -1, -1, '2026-06-26 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo E', 5, 'Curazao', 'Costa de Marfil', -1, -1, '2026-06-26 13:00:00');

-- Grupo F
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo F', 0, 'Países Bajos', 'Japón', -1, -1, '2026-06-14 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo F', 1, 'Suecia', 'Túnez', -1, -1, '2026-06-14 19:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo F', 2, 'Países Bajos', 'Suecia', -1, -1, '2026-06-20 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo F', 3, 'Japón', 'Túnez', -1, -1, '2026-06-20 19:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo F', 4, 'Países Bajos', 'Túnez', -1, -1, '2026-06-26 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo F', 5, 'Japón', 'Suecia', -1, -1, '2026-06-26 16:00:00');

-- Grupo G
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo G', 0, 'Bélgica', 'Egipto', -1, -1, '2026-06-15 10:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo G', 1, 'Irán', 'Nueva Zelanda', -1, -1, '2026-06-15 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo G', 2, 'Bélgica', 'Irán', -1, -1, '2026-06-21 10:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo G', 3, 'Egipto', 'Nueva Zelanda', -1, -1, '2026-06-21 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo G', 4, 'Bélgica', 'Nueva Zelanda', -1, -1, '2026-06-27 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo G', 5, 'Egipto', 'Irán', -1, -1, '2026-06-27 13:00:00');

-- Grupo H
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo H', 0, 'España', 'Cabo Verde', -1, -1, '2026-06-15 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo H', 1, 'Arabia Saudita', 'Uruguay', -1, -1, '2026-06-15 19:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo H', 2, 'España', 'Arabia Saudita', -1, -1, '2026-06-21 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo H', 3, 'Cabo Verde', 'Uruguay', -1, -1, '2026-06-21 19:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo H', 4, 'España', 'Uruguay', -1, -1, '2026-06-27 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo H', 5, 'Cabo Verde', 'Arabia Saudita', -1, -1, '2026-06-27 16:00:00');

-- Grupo I
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo I', 0, 'Francia', 'Senegal', -1, -1, '2026-06-16 10:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo I', 1, 'Irak', 'Noruega', -1, -1, '2026-06-16 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo I', 2, 'Francia', 'Irak', -1, -1, '2026-06-22 10:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo I', 3, 'Senegal', 'Noruega', -1, -1, '2026-06-22 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo I', 4, 'Francia', 'Noruega', -1, -1, '2026-06-28 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo I', 5, 'Senegal', 'Irak', -1, -1, '2026-06-28 13:00:00');

-- Grupo J
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo J', 0, 'Argentina', 'Argelia', -1, -1, '2026-06-16 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo J', 1, 'Austria', 'Jordania', -1, -1, '2026-06-16 19:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo J', 2, 'Argentina', 'Austria', -1, -1, '2026-06-22 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo J', 3, 'Argelia', 'Jordania', -1, -1, '2026-06-22 19:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo J', 4, 'Argentina', 'Jordania', -1, -1, '2026-06-28 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo J', 5, 'Argelia', 'Austria', -1, -1, '2026-06-28 16:00:00');

-- Grupo K
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo K', 0, 'Portugal', 'RD Congo', -1, -1, '2026-06-17 10:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo K', 1, 'Uzbekistán', 'Colombia', -1, -1, '2026-06-17 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo K', 2, 'Portugal', 'Uzbekistán', -1, -1, '2026-06-23 08:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo K', 3, 'RD Congo', 'Colombia', -1, -1, '2026-06-23 21:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo K', 4, 'Portugal', 'Colombia', -1, -1, '2026-06-29 13:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo K', 5, 'RD Congo', 'Uzbekistán', -1, -1, '2026-06-29 13:00:00');

-- Grupo L
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo L', 0, 'Inglaterra', 'Croacia', -1, -1, '2026-06-17 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo L', 1, 'Ghana', 'Panamá', -1, -1, '2026-06-17 19:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo L', 2, 'Inglaterra', 'Ghana', -1, -1, '2026-06-23 08:50:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo L', 3, 'Croacia', 'Panamá', -1, -1, '2026-06-23 09:15:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo L', 4, 'Inglaterra', 'Panamá', -1, -1, '2026-06-29 16:00:00');
INSERT IGNORE INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante, fecha_hora) VALUES ('Grupo L', 5, 'Croacia', 'Ghana', -1, -1, '2026-06-29 16:00:00');

