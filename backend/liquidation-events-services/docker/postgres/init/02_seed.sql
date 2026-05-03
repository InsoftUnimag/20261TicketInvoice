INSERT INTO eventos (id, nombre, estado) VALUES
  (1, 'Concierto Rock 2026', 'CERRADO'),
  (2, 'Festival de Teatro Nacional', 'CERRADO'),
  (3, 'Copa America - Semifinal', 'CERRADO'),
  (4, 'Expo Tecnologia 2026', 'EN_CURSO')
ON CONFLICT (id) DO NOTHING;

INSERT INTO recintos (id, nombre, tipo_recinto, tasa_comision, estado) VALUES
  (1, 'Estadio Nacional de Colombia', 'ESTADIO', 0.1200, 'ACTIVO'),
  (2, 'Teatro Colon', 'TEATRO', 0.0800, 'ACTIVO'),
  (3, 'Estadio Metropolitano', 'ESTADIO', 0.1500, 'ACTIVO'),
  (4, 'Corferias', 'TEATRO', 0.1000, 'ACTIVO')
ON CONFLICT (id) DO NOTHING;

INSERT INTO comisiones_recinto (recinto_id, tipo_comision, valor_comision) VALUES
  (1, 'PORCENTAJE', 7.50),
  (2, 'PORCENTAJE', 8.00)
ON CONFLICT (recinto_id) DO NOTHING;

INSERT INTO tickets (id, evento_id, estado_financiero, valor_ticket, estado_ingreso, fecha_hora_ingreso, tipo_acceso, condicion_liquidacion, valor_liquidacion) VALUES
  (1001, 1, 'VALIDADO', 500.00, 'CHECKED_IN', NOW() - INTERVAL '2 hour', 'INGRESO', 'VALIDADO', 500.00),
  (1002, 1, 'VALIDADO', 500.00, 'CHECKED_IN', NOW() - INTERVAL '2 hour', 'INGRESO', 'VALIDADO', 500.00),
  (1003, 1, 'NO_ASISTIO', 500.00, 'NOT_ATTENDED', NULL, NULL, 'VENDIDO', 500.00),
  (1004, 1, 'NO_ASISTIO', 500.00, 'NOT_ATTENDED', NULL, NULL, 'VENDIDO', 500.00),
  (1005, 1, 'CORTESIA', 0.00, 'CHECKED_IN', NOW() - INTERVAL '1 hour', 'CORTESIA', 'CORTESIA', 0.00),
  (1006, 1, 'CANCELADO', 500.00, 'NOT_ATTENDED', NULL, NULL, 'CANCELADO', -500.00),

  (2001, 2, 'VALIDADO', 500.00, 'CHECKED_IN', NOW() - INTERVAL '5 hour', 'INGRESO', 'VALIDADO', 500.00),
  (2002, 2, 'VALIDADO', 500.00, 'CHECKED_IN', NOW() - INTERVAL '5 hour', 'INGRESO', 'VALIDADO', 500.00),
  (2003, 2, 'NO_ASISTIO', 500.00, 'NOT_ATTENDED', NULL, NULL, 'VENDIDO', 500.00),
  (2004, 2, 'CORTESIA', 0.00, 'CHECKED_IN', NOW() - INTERVAL '4 hour', 'CORTESIA', 'CORTESIA', 0.00),
  (2005, 2, 'CANCELADO', 500.00, 'NOT_ATTENDED', NULL, NULL, 'CANCELADO', -500.00),

  (3001, 3, 'VALIDADO', 250.00, 'CHECKED_IN', NOW() - INTERVAL '3 hour', 'INGRESO', 'VALIDADO', 250.00),
  (3002, 3, 'NO_ASISTIO', 250.00, 'NOT_ATTENDED', NULL, NULL, 'VENDIDO', 250.00),
  (3003, 3, 'CORTESIA', 0.00, 'CHECKED_IN', NOW() - INTERVAL '2 hour', 'CORTESIA', 'CORTESIA', 0.00),
  (3004, 3, 'CANCELADO', 250.00, 'NOT_ATTENDED', NULL, NULL, 'CANCELADO', -250.00),

  (4001, 4, 'VALIDADO', 200.00, 'CHECKED_IN', NOW() - INTERVAL '1 hour', 'INGRESO', 'VALIDADO', 200.00),
  (4002, 4, 'NO_ASISTIO', 200.00, 'NOT_ATTENDED', NULL, NULL, 'VENDIDO', 200.00)
ON CONFLICT (id) DO NOTHING;
