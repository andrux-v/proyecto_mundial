-- Script para auto-completar los pronósticos que los usuarios no realizaron en los partidos ya cerrados.
-- Los marcadores generados serán aleatorios pero razonables (entre 0 y 3 goles).
-- Al insertar en 'apuestas', el trigger de la base de datos creará automáticamente la entrada en 'historial_apuestas'.

USE mundial_db;

INSERT INTO apuestas (usuario_documento, partido_id, goles_local, goles_visitante)
SELECT u.documento, p.id, 
       FLOOR(RAND() * 4) AS goles_local, 
       FLOOR(RAND() * 4) AS goles_visitante
FROM usuarios u
CROSS JOIN partidos p
LEFT JOIN apuestas a ON a.usuario_documento = u.documento AND a.partido_id = p.id
WHERE u.es_admin = false
  AND a.id IS NULL
  AND NOW() >= DATE_SUB(p.fecha_hora, INTERVAL 10 MINUTE);
