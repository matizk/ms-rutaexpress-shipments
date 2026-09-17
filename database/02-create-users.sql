-- Ejecutar en la base shipments_db con el usuario rutaexpress.
-- cognito_sub identifica al usuario autenticado en Cognito; nunca se almacena su contraseña.

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    cognito_sub VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(160) NOT NULL UNIQUE,
    rut VARCHAR(20),
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS ix_usuarios_rol ON usuarios (rol);

-- Después del primer login, registrar el usuario real con su sub de Cognito:
-- INSERT INTO usuarios (cognito_sub, email, rut, nombre, apellido, rol)
-- VALUES ('SUB_REAL_DE_COGNITO', 'correo@ejemplo.cl', 'RUT', 'Nombre', 'Apellido', 'Admin');
