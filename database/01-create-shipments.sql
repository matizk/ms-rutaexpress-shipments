-- Ejecutar conectado a la base shipments_db con el usuario rutaexpress.
-- En desarrollo Compose usa ddl-auto=update; este esquema es para despliegues controlados.

CREATE TABLE shipments (
    id BIGSERIAL PRIMARY KEY,
    codigo_seguimiento VARCHAR(50) NOT NULL,
    nombre_destinatario VARCHAR(120) NOT NULL,
    email_destinatario VARCHAR(160) NOT NULL,
    direccion_origen VARCHAR(250) NOT NULL,
    direccion_destino VARCHAR(250) NOT NULL,
    peso_kg NUMERIC(10, 2) NOT NULL CHECK (peso_kg > 0),
    servicio_id BIGINT,
    servicio_nombre VARCHAR(120),
    estado VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_actualizacion TIMESTAMP NOT NULL,
    CONSTRAINT uk_shipments_codigo_seguimiento UNIQUE (codigo_seguimiento),
    CONSTRAINT ck_shipments_estado CHECK (
        estado IN ('CREADO', 'ACEPTADO', 'EN_BODEGA', 'EN_RUTA', 'ENTREGADO', 'CANCELADO')
    )
);

CREATE INDEX ix_shipments_estado_fecha ON shipments (estado, fecha_creacion);
