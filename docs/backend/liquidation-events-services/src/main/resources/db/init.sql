CREATE TABLE IF NOT EXISTS recintos (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS comisiones_recinto (
    recinto_id BIGINT PRIMARY KEY REFERENCES recintos(id),
    tipo_comision VARCHAR(20) NOT NULL,
    valor_comision NUMERIC(14,2) NOT NULL
);

INSERT INTO recintos (id, nombre) VALUES
    (1, 'Recinto Centro'),
    (2, 'Recinto Norte')
ON CONFLICT (id) DO NOTHING;

INSERT INTO comisiones_recinto (recinto_id, tipo_comision, valor_comision) VALUES
    (1, 'PORCENTUAL', 7.50)
ON CONFLICT (recinto_id) DO NOTHING;
